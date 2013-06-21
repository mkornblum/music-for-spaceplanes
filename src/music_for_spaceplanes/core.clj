(ns music-for-spaceplanes.core)

;; Mark K - Originally Copypasta'd from https://github.com/overtone/overtone/wiki/Chords-and-scales

(definst square-wave [freq 220 attack 0.2 sustain 0.5 release 0.3 vol 0.70]
  (* (env-gen (lin-env attack sustain release) 1 1 0 1 FREE)
     (square freq)
     vol))

;; We can play notes using frequency in Hz
(square-wave 440)
(square-wave 523.25)
(square-wave 261.62) ; This is C4

;; We can also play notes using MIDI note values
(square-wave (midi->hz 69))
(square-wave (midi->hz 72))
(square-wave (midi->hz 60)) ; This is C4

;; We can play notes using standard music notes as well
(square-wave (midi->hz (note :A4)))
(square-wave (midi->hz (note :C5)))
(square-wave (midi->hz (note :C4))) ; This is C4! Surprised?

;; Define a function for convenience
(defn note->hz [music-note]
  (midi->hz (note music-note)))

; Slightly less to type
(square-wave (note->hz :C5))

;; Let's make it even easier
(defn square2 [music-note]
  (square-wave (midi->hz (note music-note)) 0.55)
  (square-wave (fourth (midi->hz (note music-note))))
  )

;; Great!
(square2 :A4)
(square2 :C5)
(square2 :C4)

;; Let's play some chords


;; this is one possible implementation of play-chord
(defn play-chord [a-chord]
  (doseq [note a-chord] (square2 note)))

;; We can play many types of chords.
;; For the complete list, visit https://github.com/overtone/overtone/blob/master/src/overtone/music/pitch.clj and search for "def CHORD"
(play-chord (chord :C4 :major))

;; We can play a chord progression on the synth
;; using times:
(defn chord-progression-time []
  (let [time (now)]
    (at time (play-chord (chord :C4 :major)))
    (at (+ 2000 time) (play-chord (chord :G3 :major)))
    (at (+ 3000 time) (play-chord (chord :F3 :sus4)))
    (at (+ 4300 time) (play-chord (chord :F3 :major)))
    (at (+ 5000 time) (play-chord (chord :G3 :major)))))

(chord-progression-time)

;; or beats:
(defonce metro (metronome 120))
(metro)
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 14 beat-num)) (play-chord (chord :F3 :major)))
)

(chord-progression-beat metro (metro))

;; We can use recursion to keep playing the chord progression
(defn chord-progression-beat [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 4 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :A3 :minor)))
  (at (m (+ 12 beat-num)) (play-chord (chord :F3 :major)))
  (apply-at (m (+ 16 beat-num)) chord-progression-beat m (+ 16 beat-num) [])
)
(chord-progression-beat metro (metro))


;; this part is original

(defn riff [m beat-num]
  (at (m (+ 0 beat-num)) (square2 :E3))
  (at (m (+ 2 beat-num)) (square2 :A2))
  (at (m (+ 4 beat-num)) (square2 :G3))
  (at (m (+ 6 beat-num)) (square2 :C3))
  (at (m (+ 8 beat-num)) (square2 :D3))
  (at (m (+ 10 beat-num)) (square2 :G2))
  (at (m (+ 12 beat-num)) (square2 :D3))
  (at (m (+ 13 beat-num)) (square2 :C3))
  (at (m (+ 14 beat-num)) (square2 :B2))
  (at (m (+ 15 beat-num)) (square2 :C3))
  (apply-at (m (+ 16 beat-num)) riff m (+ 16 beat-num) [])
)

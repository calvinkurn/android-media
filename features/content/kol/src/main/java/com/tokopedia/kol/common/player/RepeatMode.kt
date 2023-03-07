package com.tokopedia.kol.common.player

object RepeatMode {
    /**
     * Normal playback without repetition.
     */
    val REPEAT_MODE_OFF = 0
    /**
     * "Repeat One" mode to repeat the currently playing window infinitely.
     */
    val REPEAT_MODE_ONE = 1
    /**
     * "Repeat All" mode to repeat the entire timeline infinitely.
     */
    val REPEAT_MODE_ALL = 2
}

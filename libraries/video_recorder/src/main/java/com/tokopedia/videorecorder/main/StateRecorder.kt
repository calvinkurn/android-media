package com.tokopedia.videorecorder.main

/**
 * Created by isfaaghyth on 10/04/19.
 * github: @isfaaghyth
 */
sealed class StateRecorder {
    object Start: StateRecorder()
    object Stop: StateRecorder()
}
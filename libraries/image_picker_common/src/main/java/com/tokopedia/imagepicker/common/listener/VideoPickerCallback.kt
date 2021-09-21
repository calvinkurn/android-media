package com.tokopedia.imagepicker.common.listener

import com.tokopedia.imagepicker.common.state.StateRecorder

/**
 * Created by isfaaghyth on 01/03/19.
 * github: @isfaaghyth
 */
interface VideoPickerCallback {
    fun onVideoTaken(filePath: String)
    fun onPreviewVideoVisible()
    fun onVideoVisible()
    fun onVideoRecorder(state: StateRecorder)
}
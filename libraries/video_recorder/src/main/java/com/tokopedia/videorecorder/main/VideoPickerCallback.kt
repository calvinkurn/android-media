package com.tokopedia.videorecorder.main

/**
 * Created by isfaaghyth on 01/03/19.
 * github: @isfaaghyth
 */
interface VideoPickerCallback {
    fun onVideoTaken(filePath: String)
    fun onPreviewVideoVisible()
    fun onVideoVisible()
}
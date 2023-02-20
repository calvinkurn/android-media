package com.tokopedia.kol.common.util

import android.app.Activity
import android.util.DisplayMetrics
import kotlin.math.ceil

data class VideoSize(
    val videoWidth: Int,
    val videoHeight: Int
)

fun resize(activity: Activity, mVideoWidth: Int, mVideoHeight: Int): VideoSize {
    var videoWidth = mVideoWidth
    var videoHeight = mVideoHeight
    val displayMetrics = DisplayMetrics()
    activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

    val heightRatio = videoHeight.toFloat() / displayMetrics.widthPixels.toFloat()
    val widthRatio = videoWidth.toFloat() / displayMetrics.heightPixels.toFloat()

    if (videoWidth > videoHeight) {
        videoWidth = ceil((videoWidth.toFloat() * widthRatio).toDouble()).toInt()
        videoHeight = ceil((videoHeight.toFloat() * widthRatio).toDouble()).toInt()
    } else {
        videoWidth = ceil((videoWidth.toFloat() * heightRatio).toDouble()).toInt()
        videoHeight = ceil((videoHeight.toFloat() * heightRatio).toDouble()).toInt()
    }

    return VideoSize(videoWidth, videoHeight)
}

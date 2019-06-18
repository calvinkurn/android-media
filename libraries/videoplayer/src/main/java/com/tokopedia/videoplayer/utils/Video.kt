package com.tokopedia.videoplayer.utils

import android.app.Activity
import android.util.DisplayMetrics
import com.tokopedia.videoplayer.data.VideoSize

/**
 * Created by isfaaghyth on 08/04/19.
 * github: @isfaaghyth
 */
object Video {

    fun resize(activity: Activity, mVideoWidth: Int, mVideoHeight: Int): VideoSize {
        var videoWidth = mVideoWidth
        var videoHeight = mVideoHeight
        val displaymetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displaymetrics)

        val heightRatio = videoHeight.toFloat() / displaymetrics.widthPixels.toFloat()
        val widthRatio = videoWidth.toFloat() / displaymetrics.heightPixels.toFloat()

        if (videoWidth > videoHeight) {
            videoWidth = Math.ceil((videoWidth.toFloat() * widthRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * widthRatio).toDouble()).toInt()
        } else {
            videoWidth = Math.ceil((videoWidth.toFloat() * heightRatio).toDouble()).toInt()
            videoHeight = Math.ceil((videoHeight.toFloat() * heightRatio).toDouble()).toInt()
        }

        return VideoSize(videoWidth, videoHeight)
    }

}
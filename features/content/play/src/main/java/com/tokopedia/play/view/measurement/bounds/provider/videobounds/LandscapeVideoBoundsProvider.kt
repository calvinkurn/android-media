package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import android.view.View
import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
class LandscapeVideoBoundsProvider : VideoBoundsProvider {

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return 0
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(
        view: View,
        estimatedKeyboardHeight: Int,
        videoOrientation: VideoOrientation,
    ): Int {
        return 0
    }
}
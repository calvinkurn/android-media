package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
class LandscapeVideoBoundsProvider : VideoBoundsProvider {

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return 0
    }

    override suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return 0
    }
}
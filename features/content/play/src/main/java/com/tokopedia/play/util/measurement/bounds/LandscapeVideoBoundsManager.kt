package com.tokopedia.play.util.measurement.bounds

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
class LandscapeVideoBoundsManager : VideoBoundsManager {

    override suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return 0
    }

    override fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return 0
    }
}
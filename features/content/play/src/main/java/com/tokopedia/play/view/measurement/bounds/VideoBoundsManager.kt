package com.tokopedia.play.view.measurement.bounds

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
interface VideoBoundsManager {

    suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int

    suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int
}
package com.tokopedia.play.view.measurement.bounds.provider.videobounds

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/08/20
 */
interface VideoBoundsProvider {

    suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int

    suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int
}
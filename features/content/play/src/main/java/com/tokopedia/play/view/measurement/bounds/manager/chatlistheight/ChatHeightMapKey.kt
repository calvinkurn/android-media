package com.tokopedia.play.view.measurement.bounds.manager.chatlistheight

import com.tokopedia.play.view.type.VideoOrientation

/**
 * Created by jegul on 04/09/20
 */
data class ChatHeightMapKey(
        val videoOrientation: VideoOrientation,
        val maxTop: Int?,
        val hasQuickReply: Boolean?,
        val hasProductFeatured: Boolean?,
        val hasPinnedVoucher: Boolean?
)

data class ChatHeightMapValue(
        val height: Float,
        val consistency: Int
)
package com.tokopedia.play.view.uimodel

import com.tokopedia.play.ui.toolbar.model.PartnerType

/**
 * Created by jegul on 16/12/19
 */
data class ChannelInfoUiModel(
        val channelId: String,
        val title: String,
        val description: String,
        val videoStream: VideoStreamUiModel,
        val partner: PartnerUiModel,
        val totalView: String,
        val pinnedMessage: PinnedMessageUiModel?,
        val quickReply: List<String>
)

data class PartnerUiModel(
        val partnerType: PartnerType,
        val partnerId: Long
)
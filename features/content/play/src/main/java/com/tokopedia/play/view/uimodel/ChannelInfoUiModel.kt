package com.tokopedia.play.view.uimodel

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PlayVideoType

/**
 * Created by jegul on 16/12/19
 */
data class ChannelInfoUiModel(
        val channelId: String,
        val title: String,
        val description: String,
        val videoType: PlayVideoType,
        val partner: PartnerUiModel,
        val totalView: String,
        val pinnedMessage: PinnedMessageUiModel?,
        val quickReply: List<String>,
        val isActive: Boolean
)

data class PartnerUiModel(
        val partnerType: PartnerType,
        val partnerId: Long
)

data class PinnedMessageUiModel(
        val applink: String,
        val title: String,
        val message: String,
        val shouldRemove: Boolean
)
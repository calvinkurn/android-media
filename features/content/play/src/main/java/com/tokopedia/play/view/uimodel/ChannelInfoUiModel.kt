package com.tokopedia.play.view.uimodel

import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PlayChannelType

/**
 * Created by jegul on 16/12/19
 */
data class ChannelInfoUiModel(
        val id: String,
        val title: String,
        val description: String,
        val partnerId: Long,
        val partnerType: PartnerType,
        val moderatorName: String,
        val contentId: Int,
        val contentType: Int,
        val likeType: Int,
        val isShowCart: Boolean
)
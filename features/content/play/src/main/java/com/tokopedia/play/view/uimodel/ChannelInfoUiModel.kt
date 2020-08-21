package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 16/12/19
 */
data class ChannelInfoUiModel(
        val id: String,
        val showCart: Boolean,
        val showPinnedProduct: Boolean,
        val titleBottomSheet: String,
        val partnerInfo: PartnerInfoUiModel,
        val feedInfo: FeedInfoUiModel
)

data class FeedInfoUiModel(
        val contentId: String,
        val contentType: Int,
        val likeType: Int
)
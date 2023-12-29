package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.model.type.FeedContentType

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
data class FeedCardLivePreviewContentModel(
    override val id: String,
    val typename: String,
    val type: String,
    val author: FeedAuthorModel,
    val title: String,
    val subtitle: String,
    val text: String,
    val applink: String,
    val media: List<FeedMediaModel>,
    val hashtagApplinkFmt: String,
    val hashtagWeblinkFmt: String,
    val playChannelId: String,
    val followers: FeedFollowModel,
    val detailScore: List<FeedScoreModel>,
    val hasVoucher: Boolean,
    val campaign: FeedCardCampaignModel,
    val products: List<FeedCardProductModel>,
    val isLive: Boolean = true,
    override val contentType: FeedContentType = FeedContentType.PlayLivePreview,
    override val share: FeedShareModel? = null
) : Visitable<FeedAdapterTypeFactory>, FeedContentUiModel {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    val contentScore = detailScore.firstOrNull { it.isContentScore }?.value ?: ""

    val videoUrl: String = media.firstOrNull()?.mediaUrl.orEmpty()
}

package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
data class FeedCardLivePreviewContentModel(
    val id: String,
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
    val products: List<FeedCardProductModel>
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    val contentScore = detailScore.firstOrNull { it.isContentScore }?.value ?: ""

    val videoUrl: String = media.firstOrNull()?.mediaUrl.orEmpty()
}

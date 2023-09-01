package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
data class FeedCardVideoContentModel(
    val id: String,
    val typename: String,
    val type: String,
    val author: FeedAuthorModel,
    val title: String,
    val subtitle: String,
    val text: String,
    val cta: FeedCardCtaModel,
    val applink: String,
    val weblink: String,
    val actionButtonLabel: String,
    val campaign: FeedCardCampaignModel,
    val hasVoucher: Boolean,
    val products: List<FeedCardProductModel>,
    val totalProducts: Int,
    val media: List<FeedMediaModel>,
    val hashtagApplinkFmt: String,
    val hashtagWeblinkFmt: String,
    val views: FeedViewModel,
    val like: FeedLikeModel,
    val comments: FeedCommentModel,
    val share: FeedShareModel,
    val followers: FeedFollowModel,
    val menuItems: List<ContentMenuItem>,
    val detailScore: List<FeedScoreModel>,
    val publishedAt: String,
    val playChannelId: String
) : Visitable<FeedAdapterTypeFactory> {

    val isTypeProductHighlight: Boolean
        get() = typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT

    val isPlayContent: Boolean
        get() = typename == TYPE_FEED_X_CARD_PLAY

    val contentScore = detailScore.firstOrNull { it.isContentScore }?.value
        ?: if (detailScore.isNotEmpty()) detailScore.first().value else ""

    val videoUrl: String = media.firstOrNull()?.mediaUrl.orEmpty()

    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)
}

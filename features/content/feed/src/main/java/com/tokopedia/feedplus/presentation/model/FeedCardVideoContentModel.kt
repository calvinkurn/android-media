package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.content.common.report_content.model.FeedMenuItem
import com.tokopedia.explore.view.adapter.Empty
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_PLAY
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import com.tokopedia.feedplus.presentation.model.type.AuthorType

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
    val menuItems: List<FeedMenuItem>,
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

    companion object {
        val Empty get() = FeedCardVideoContentModel(id = "", typename = "", type = "",
            author = FeedAuthorModel(id = "", type = AuthorType.Unknown, name = "", badgeUrl = "", logoUrl = "", appLink = "", encryptedUserId = "", isLive = false),
            title = "", subtitle = "", text = "", cta = FeedCardCtaModel(), applink = "", weblink = "", actionButtonLabel = "", campaign = FeedCardCampaignModel(), hasVoucher = false,
            products = emptyList(), totalProducts = 0, media = emptyList(), hashtagApplinkFmt = "", hashtagWeblinkFmt = "", views = FeedViewModel(), like = FeedLikeModel(), comments = FeedCommentModel(),
            share = FeedShareModel(contentId = "", author = FeedAuthorModel(id = "", type = AuthorType.Unknown, name = "", badgeUrl = "", logoUrl = "", appLink = "", encryptedUserId = "", isLive = false), appLink = "", webLink = "", mediaUrl = ""),
            followers = FeedFollowModel(), menuItems = emptyList(), detailScore = emptyList(), publishedAt = "", playChannelId = "",
        )
    }
}

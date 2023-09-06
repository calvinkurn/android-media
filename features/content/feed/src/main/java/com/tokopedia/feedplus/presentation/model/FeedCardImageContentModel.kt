package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_TOP_ADS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PLACEHOLDER
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory

/**
 * Created By : Muhammad Furqan on 02/03/23
 */
data class FeedCardImageContentModel(
    val id: String,
    val typename: String,
    val type: String,
    val author: FeedAuthorModel,
    val title: String,
    val subtitle: String,
    val text: String,
    val cta: FeedCardCtaModel,
    val ribbonImageUrl: String,
    val applink: String,
    val weblink: String,
    val applinkProductList: String,
    val actionButtonLabel: String,
    val products: List<FeedCardProductModel>,
    val totalProducts: Int,
    val campaign: FeedCardCampaignModel,
    val hasVoucher: Boolean,
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
    val maxDiscountPercentage: Int,
    val maxDiscountPercentageFmt: String,
    val adViewUri: String = "", // use only for topads
    val adViewUrl: String = "", // use only for topads
    val isFetched: Boolean = false, // use only for topads
    val topAdsId: String = "" // use only for topads
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    val isTypeProductHighlight: Boolean
        get() = typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT

    val isTopAds: Boolean
        get() = type == TYPE_FEED_TOP_ADS && typename == TYPE_FEED_X_CARD_PLACEHOLDER

    val showComment: Boolean
        get() = !isTopAds && !isTypeProductHighlight

    val contentScore = detailScore.firstOrNull { it.isContentScore }?.value ?: ""
}

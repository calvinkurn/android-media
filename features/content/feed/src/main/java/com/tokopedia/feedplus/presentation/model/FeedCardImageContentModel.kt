package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_LONG_VIDEO
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_TOP_ADS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_PLACEHOLDER
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_X_CARD_POST
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
    val reportable: Boolean,
    val editable: Boolean,
    val deletable: Boolean,
    val detailScore: List<FeedScoreModel>,
    val publishedAt: String,
    val maxDiscountPercentage: Int,
    val maxDiscountPercentageFmt: String,
    val adViewUri: String = "",  // use only for topads
    val adViewUrl: String = "",  // use only for topads
    val isFetched: Boolean = false // use only for topads
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    val isTypeCardPlaceholder: Boolean
        get() = typename == TYPE_FEED_X_CARD_PLACEHOLDER

    val isTypeProductHighlight: Boolean
        get() = typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT

    val isTypeSGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && media.isNotEmpty() &&
            media.first().type != TYPE_FEED_LONG_VIDEO && author.type == AUTHOR_SGC

    val isTypeUGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && author.type == AUTHOR_UGC

    val isTopAds: Boolean
        get() = type == TYPE_FEED_TOP_ADS && typename == TYPE_FEED_X_CARD_PLACEHOLDER

    companion object {
        private const val AUTHOR_SGC = 2
        private const val AUTHOR_UGC = 3
    }
}

package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
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
    val applinkProductList: String,
    val actionButtonLabel: String,
    val products: List<FeedCardProductModel>,
    val totalProducts: Int,
    val campaign: FeedCardCampaignModel,
    val hasVoucher: Boolean,
    val media: List<FeedMediaModel>,
    val tags: List<FeedCardProductModel>,
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
    val maxDiscountPercentageFmt: String
) : Visitable<FeedAdapterTypeFactory> {
    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    val isTypeCardPlaceholder: Boolean
        get() = typename == TYPE_FEED_X_CARD_PLACEHOLDER

    val isTypeProductHighlight: Boolean
        get() = typename == TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT

    val isTypeSGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && media.isNotEmpty() &&
            media.first().type != TYPE_LONG_VIDEO && author.type == AUTHOR_SGC

    val isTypeUGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && author.type == AUTHOR_UGC

    companion object {
        const val TYPE_FEED_X_CARD_POST = "FeedXCardPost"
        const val TYPE_FEED_X_CARD_PLACEHOLDER = "FeedXCardPlaceholder"
        const val TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT = "FeedXCardProductsHighlight"
        const val TYPE_LONG_VIDEO: String = "long-video"

        private const val AUTHOR_SGC = 2
        private const val AUTHOR_UGC = 3
    }
}

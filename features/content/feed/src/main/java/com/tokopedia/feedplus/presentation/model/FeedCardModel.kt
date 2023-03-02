package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedCardModel(
    val id: String,
    val mods: List<String>,
    val typename: String,
    val type: String = "",
    val author: FeedAuthorModel = FeedAuthorModel(),
    val title: String = "",
    val subtitle: String = "",
    val text: String = "",
    val cta: FeedCardCtaModel = FeedCardCtaModel(),
    val ribbonImageUrl: String = "",
    val applink: String = "",
    val weblink: String = "",
    val applinkProductList: String = "",
    val weblinkProductList: String = "",
    val actionButtonLabel: String = "",
    val products: List<FeedCardProductModel> = emptyList(),
    val totalProducts: Int = 0,
    val campaign: FeedCardCampaignModel = FeedCardCampaignModel(),
    val hasVoucher: Boolean = false,
    val actionButtonOperationWeb: String = "",
    val actionButtonOperationApp: String = "",
    val media: List<FeedMediaModel> = emptyList(),
    val mediaRatio: FeedMediaRatioModel = FeedMediaRatioModel(),
    val tags: List<FeedCardProductModel> = emptyList(),
    val hashtagApplinkFmt: String = "",
    val hashtagWeblinkFmt: String = "",
    val views: FeedViewModel = FeedViewModel(),
    val like: FeedLikeModel = FeedLikeModel(),
    val comments: FeedCommentModel = FeedCommentModel(),
    val share: FeedShareModel = FeedShareModel(),
    val followers: FeedFollowModel = FeedFollowModel(),
    val reportable: Boolean = false,
    val editable: Boolean = false,
    val deletable: Boolean = false,
    val playChannelId: String = "",
    val detailScore: List<FeedScoreModel> = emptyList(),
    val publishedAt: String = "",
    val promos: List<String> = emptyList(),
    val items: List<FeedCardItemModel> = emptyList(),
    val maximumDiscountPercentage: Int = 0,
    val maximumDiscountPercentageFmt: String = ""
) {
//) : Visitable<FeedAdapterTypeFactory> {

//    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)


    val isTypeVOD: Boolean
        get() = typename == TYPE_FEED_X_CARD_PLAY

    val isTypeLongVideo: Boolean
        get() = media.isNotEmpty() && media.first().type == TYPE_LONG_VIDEO

    val isTypeSgcVideo: Boolean
        get() = media.isNotEmpty() && media.first().type == TYPE_VIDEO


    val isASGCDiscountToko: Boolean
        get() = type == ASGC_DISCOUNT_TOKO

    val contentScore
        get() = detailScore.filter {
            it.isContentScore
        }

    companion object {
        private const val TYPE_FEED_X_CARD_PLAY = "FeedXCardPlay"

        private const val TYPE_LONG_VIDEO: String = "long-video"
        private const val TYPE_VIDEO: String = "video"

        private const val ASGC_DISCOUNT_TOKO = "asgc_discount_toko"
    }
}

data class FeedCardItemModel(
    val id: String,
    val applink: String = "",
    val weblink: String = "",
    val coverUrl: String = "",
    val product: FeedCardProductModel = FeedCardProductModel()
)

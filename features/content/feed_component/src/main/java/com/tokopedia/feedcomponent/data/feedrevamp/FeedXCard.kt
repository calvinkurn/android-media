package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.domain.mapper.TYPE_FEED_X_CARD_POST
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product

data class FeedXCard(
    @SerializedName("__typename")
    var typename: String = "",

    //FeedXCardBanners Data Type
    @SerializedName("id")
    var id: String = "",
    @SerializedName("publishedAt")
    var publishedAt: String = "",
    @SerializedName("reportable")
    var reportable: Boolean = true,
    @SerializedName("editable")
    var editable: Boolean = false,
    @SerializedName("deletable")
    var deletable: Boolean = false,
    @SerializedName("mods")
    var mods: List<String> = emptyList(),
    @SerializedName("detailScore")
    var detailScore: List<FeedXScore> = emptyList(),

    //FeedXCardTopAds Data Type
    @SerializedName("promos")
    var promos: List<String> = emptyList(),
    @SerializedName("author")
    var author: FeedXAuthor = FeedXAuthor(),
    @SerializedName("items")
    var items: List<FeedXCardDataItem> = emptyList(),

    //FeedXCardPlaceHolder Data Type
    @SerializedName("type")
    var type: String = "",

    //FeedXCardProductsHighlight Data Type
    @SerializedName("products")
    var products: List<FeedXProduct> = emptyList(),
    @SerializedName("hasVoucher")
    var hasVoucher: Boolean = false,
    @SerializedName("subTitle")
    var subTitle: String = "",
    @SerializedName("totalProducts")
    var totalProducts: Int = 0,
    @SerializedName("cta")
    val cta: FeedXCta = FeedXCta(),
    @SerializedName("ribbonImageURL")
    val ribbonImageURL: String = "",
    @SerializedName("campaign")
    val campaign: FeedXCampaign = FeedXCampaign(),
    @SerializedName("text")
    var text: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("like")
    var like: FeedXLike = FeedXLike(),
    @SerializedName("comments", alternate = ["comm"])
    var comments: FeedXComments = FeedXComments(),
    @SerializedName("share", alternate = ["sh"])
    var share: FeedXShare = FeedXShare(),
    @SerializedName("followers", alternate = ["fol"])
    var followers: FeedXFollowers = FeedXFollowers(),
    @SerializedName("maximumDiscountPercentage")
    var maxDiscPercent: Int = 0,
    @SerializedName("maximumDiscountPercentageFmt")
    var maximumDisPercentFmt: String = "",

    //FeedXCardPost Data Type
    @SerializedName("appLink")
    var appLink: String = "",
    @SerializedName("webLink")
    var webLink: String = "",
    @SerializedName("appLinkProductList")
    var appLinkProductList: String = "",
    @SerializedName("webLinkProductList")
    var webLinkProductList: String = "",
    @SerializedName("actionButtonLabel")
    var actionButtonLabel: String = "",
    @SerializedName("actionButtonOperationWeb")
    var actionButtonOperationWeb: String = "",
    @SerializedName("actionButtonOperationApp")
    var actionButtonOperationApp: String = "",
    @SerializedName("media")
    var media: List<FeedXMedia> = emptyList(),
    @SerializedName("tags")
    var tags: List<FeedXProduct> = emptyList(),
    @SerializedName("hashtagAppLinkFmt")
    var hashtagAppLinkFmt: String = "",
    @SerializedName("hashtagWebLinkFmt")
    var hashtagWebLinkFmt: String = "",
    val impressHolder: ImpressHolder = ImpressHolder(),
    //Active carousel index
    var lastCarouselIndex: Int = 0,
    var isAsgcColorChangedAsPerWidgetColor: Boolean = false,
    //Topads
    val isTopAds: Boolean = false,
    val shopId: String = "",
    val adId: String = "",
    val adClickUrl: String = "",
    val adViewUrl: String = "",
    val cpmData: CpmData = CpmData(),
    val listProduct: List<Product> = listOf(),

    //FeedXCardPlay data type
    @SerializedName("playChannelID")
    var playChannelID: String = "",
    @SerializedName("mediaRatio")
    var mediaRatio: FeedXMediaRatio = FeedXMediaRatio(),
    @SerializedName("views")
    var views: FeedXViews = FeedXViews(),


    ) : ImpressHolder() {

    val isTypeProductHighlight: Boolean
        get() = typename == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT

    val isTypeVOD: Boolean
        get() = typename == TYPE_FEED_X_CARD_VOD
    val isTypeLongVideo: Boolean
        get() = media.isNotEmpty() && media.first().type == TYPE_LONG_VIDEO
    val isTypeSgcVideo: Boolean
        get() = media.isNotEmpty() && media.first().type == TYPE_VIDEO
    val isTypeSGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && media.isNotEmpty()
            && media.first().type != TYPE_LONG_VIDEO && author.type == AUTHOR_SGC
    val isTypeUGC: Boolean
        get() = typename == TYPE_FEED_X_CARD_POST && author.type == AUTHOR_UGC
    val useASGCNewDesign: Boolean
        get() = mods.contains(USE_ASGC_NEW_DESIGN)
    val isASGCDiscountToko: Boolean
         get() = type == ASGC_DISCOUNT_TOKO
    val contentScore
        get() = detailScore.filter { feedXScore ->
            feedXScore.isContentScore
        }

    fun copyPostData(): FeedXCard {
        return FeedXCard(
            typename = typename,
            id = id,
            type = type,
            hasVoucher = hasVoucher,
            playChannelID = playChannelID,
            mediaRatio = mediaRatio,
            author = author,
            title = title,
            totalProducts = totalProducts,
            products = products,
            subTitle = subTitle,
            text = text,
            cta = cta,
            ribbonImageURL = ribbonImageURL,
            campaign = campaign,
            deletable = deletable,
            appLink = appLink,
            webLink = webLink,
            webLinkProductList = webLinkProductList,
            appLinkProductList = appLinkProductList,
            actionButtonLabel = actionButtonLabel,
            actionButtonOperationApp = actionButtonOperationApp,
            actionButtonOperationWeb = actionButtonOperationWeb,
            media = media,
            tags = tags,
            hashtagAppLinkFmt = hashtagAppLinkFmt,
            hashtagWebLinkFmt = hashtagWebLinkFmt,
            views = views,
            like = like,
            comments = comments,
            share = share,
            followers = followers,
            maximumDisPercentFmt = maximumDisPercentFmt,
            maxDiscPercent = maxDiscPercent,
            publishedAt = publishedAt,
            mods = mods,
            detailScore = detailScore,
            impressHolder = impressHolder,
            isTopAds = isTopAds,
            adViewUrl = adViewUrl,
            adClickUrl = adClickUrl,
            cpmData = cpmData,
            listProduct = listProduct
        )
    }

    companion object {
        private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT = "FeedXCardProductsHighlight"
        private const val TYPE_FEED_X_CARD_VOD = "FeedXCardPlay"
        private const val TYPE_LONG_VIDEO: String = "long-video"
        private const val TYPE_VIDEO: String = "video"

        private const val USE_ASGC_NEW_DESIGN: String = "use_new_design"
        private const val ASGC_DISCOUNT_TOKO = "asgc_discount_toko"
        private const val AUTHOR_SGC = 2
        private const val AUTHOR_UGC = 3
    }
}

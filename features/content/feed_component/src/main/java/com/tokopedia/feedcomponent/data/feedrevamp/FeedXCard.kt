package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName
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
    @SerializedName("subTitle")
    var subTitle: String = "",
    @SerializedName("totalProducts")
    var totalProducts: Int = 0,
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

    //FeedXCardPost Data Type
    @SerializedName("appLink")
    var appLink: String = "",
    @SerializedName("webLink")
    var webLink: String = "",
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
    var lastCarouselIndex : Int = 0,
    //Topads
    val isTopAds: Boolean = false,
    val shopId: String = "",
    val adId: String = "",
    val adClickUrl:String="",
    val adViewUrl:String="",
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
    fun copyPostData(): FeedXCard {
        return FeedXCard(
            typename = typename,
            id = id,
            type = type,
            playChannelID = playChannelID,
            mediaRatio = mediaRatio,
            author = author,
            title = title,
            totalProducts = totalProducts,
            products = products,
            subTitle = subTitle,
            text = text,
            deletable = deletable,
            appLink = appLink,
            webLink = webLink,
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
            publishedAt = publishedAt,
            mods = mods,
            impressHolder = impressHolder,
            isTopAds = isTopAds,
            adViewUrl = adViewUrl,
            adClickUrl = adClickUrl,
            cpmData = cpmData,
            listProduct = listProduct
        )
    }
}
package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Muhammad Furqan on 24/02/23
 */
class FeedXHomeEntity(
    @SerializedName("items")
    val items: List<FeedXCard> = emptyList(),
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("pagination")
    val pagination: FeedXPaginationInfo = FeedXPaginationInfo()
) {
    class Response(
        @SerializedName("feedXHome")
        val feedXHome: FeedXHomeEntity = FeedXHomeEntity()
    )
}

class FeedXCard(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("type")
    val type: String = "",

    @SerializedName("items")
    val items: List<FeedXCardItem> = emptyList(),
    @SerializedName("publishedAt")
    val publishedAt: String = "",

    // FeedXCard Common (used by FeedXCardPost, FeedXCardPlay, FeedXCardTopAds, FeedXCardProductsHighlight)
    @SerializedName("author")
    val author: FeedXAuthor = FeedXAuthor(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subTitle")
    val subtitle: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("appLink")
    val applink: String = "",
    @SerializedName("webLink")
    val weblink: String = "",
    @SerializedName("media")
    val media: List<FeedXMedia> = emptyList(),
    @SerializedName("like")
    val like: FeedXLike = FeedXLike(),
    @SerializedName("comments")
    val comments: FeedXComments = FeedXComments(),
    @SerializedName("share")
    val share: FeedXShare = FeedXShare(),
    @SerializedName("followers")
    val followers: FeedXFollow = FeedXFollow(),
    @SerializedName("editable")
    val editable: Boolean = false,
    @SerializedName("deletable")
    val deletable: Boolean = false,
    @SerializedName("detailScore")
    val detailScore: List<FeedXScore> = emptyList(),

    // FeedXCard Common (used by FeedXCardPost, FeedXCardPlay)
    @SerializedName("actionButtonLabel")
    val actionButtonLabel: String = "",
    @SerializedName("actionButtonOperationWeb")
    val actionButtonOperationWeb: String = "",
    @SerializedName("actionButtonOperationApp")
    val actionButtonOperationApp: String = "",
    @SerializedName("mediaRatio")
    val mediaRatio: FeedXMediaRatio = FeedXMediaRatio(),
    @SerializedName("tags")
    val tags: List<FeedXProduct> = emptyList(),
    @SerializedName("hashtagAppLinkFmt")
    val hashtagApplinkFmt: String = "",
    @SerializedName("hashtagWebLinkFmt")
    val hashtagWeblinkFmt: String = "",
    @SerializedName("views")
    val views: FeedXView = FeedXView(),
    @SerializedName("reportable")
    val reportable: Boolean = false,

    // FeedXCardPlay
    @SerializedName("playChannelID")
    val playChannelId: String = "",

    // FeedXCardTopAds
    @SerializedName("promos")
    val promos: List<String> = emptyList(),

    // FeedXCardProductsHighlight
    @SerializedName("cta")
    val cta: FeedXCta = FeedXCta(),
    @SerializedName("ribbonImageURL")
    val ribbonImageUrl: String = "",
    @SerializedName("appLinkProductList")
    val applinkProductList: String = "",
    @SerializedName("webLinkProductList")
    val weblinkProductList: String = "",
    @SerializedName("products")
    val products: List<FeedXProduct> = emptyList(),
    @SerializedName("totalProducts")
    val totalProducts: Int = 0,
    @SerializedName("campaign")
    val campaign: FeedXCampaign = FeedXCampaign(),
    @SerializedName("hasVoucher")
    val hasVoucher: Boolean = false,
    @SerializedName("maximumDiscountPercentage")
    val maximumDiscountPercentage: Int = 0,
    @SerializedName("maximumDiscountPercentageFmt")
    val maximumDiscountPercentageFmt: String = ""

)

class FeedXCardItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("webLink")
    val weblink: String = "",
    @SerializedName("appLink")
    val applink: String = "",
    @SerializedName("coverURL")
    val coverUrl: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),

    // FeedXTopAdsItem
    @SerializedName("product")
    val product: FeedXProduct = FeedXProduct()
)

class FeedXProduct(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("shopID")
    val shopId: String = "",
    @SerializedName("shopName")
    val shopName: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("coverURL")
    val coverUrl: String = "",
    @SerializedName("webLink")
    val weblink: String = "",
    @SerializedName("appLink")
    val applink: String = "",
    @SerializedName("star")
    val star: Double = 0.0,
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("priceFmt")
    val priceFmt: String = "",
    @SerializedName("isDiscount")
    val isDiscount: Boolean = false,
    @SerializedName("discount")
    val discount: Double = 0.0,
    @SerializedName("discountFmt")
    val discountFmt: String = "",
    @SerializedName("priceOriginal")
    val priceOriginal: Double = 0.0,
    @SerializedName("priceOriginalFmt")
    val priceOriginalFmt: String = "",
    @SerializedName("priceDiscount")
    val priceDiscount: Double = 0.0,
    @SerializedName("pricDiscountFmt")
    val priceDiscountFmt: String = "",
    @SerializedName("totalSold")
    val totalSold: Int = 0,
    @SerializedName("isBebasOngkir")
    val isBebasOngkir: Boolean = false,
    @SerializedName("bebasOngkirStatus")
    val bebasOngkirStatus: String = "",
    @SerializedName("bebasOngkirURL")
    val bebasOngkirUrl: String = "",
    @SerializedName("priceMasked")
    val priceMasked: Double = 0.0,
    @SerializedName("priceMaskedFmt")
    val priceMaskedFmt: String = "",
    @SerializedName("stockWording")
    val stockWording: String = "",
    @SerializedName("stockSoldPercentage")
    val stockSoldPercentage: String = "",
    @SerializedName("cartable")
    val cartable: Boolean = false,
    @SerializedName("isCashback")
    val isCashback: Boolean = false,
    @SerializedName("cashbackFmt")
    val cashbackFmt: String = ""
)

class FeedXMedia(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("coverURL")
    val coverUrl: String = "",
    @SerializedName("mediaURL")
    val mediaUrl: String = "",
    @SerializedName("appLink")
    val applink: String = "",
    @SerializedName("webLink")
    val weblink: String = "",
    @SerializedName("tagging")
    val tagging: List<FeedXMediaTagging> = emptyList(),
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXMediaTagging(
    @SerializedName("tagIndex")
    val tagIndex: Int = 0,
    @SerializedName("posX")
    val posX: Double = 0.0,
    @SerializedName("posY")
    val posY: Double = 0.0
)

class FeedXMediaRatio(
    @SerializedName("width")
    val width: Int = 0,
    @SerializedName("height")
    val height: Int = 0
)

class FeedXView(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXLike(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("likedBy")
    val likedBy: List<String> = emptyList(),
    @SerializedName("isLiked")
    val isLiked: Boolean = false,
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXComments(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("items")
    val items: List<FeedXCommentItem> = emptyList()
)

class FeedXCommentItem(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("author")
    val author: FeedXAuthor = FeedXAuthor(),
    @SerializedName("text")
    val text: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXShare(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("operation")
    val operation: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXFollow(
    @SerializedName("isFollowed")
    val isFollowed: Boolean = false,
    @SerializedName("label")
    val label: String = "",
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList()
)

class FeedXAuthor(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("badgeURL")
    val badgeUrl: String = "",
    @SerializedName("logoURL")
    val logoUrl: String = "",
    @SerializedName("webLink")
    val weblink: String = "",
    @SerializedName("appLink")
    val applink: String = "",
    @SerializedName("encryptedUserID")
    val encryptedUserId: String = "",
    @SerializedName("isLive")
    val isLive: Boolean = false
)

class FeedXScore(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("value")
    val value: String = ""
)

class FeedXCta(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("subtitle")
    val subtitle: List<String> = emptyList(),
    @SerializedName("color")
    val color: String = "",
    @SerializedName("colorGradient")
    val colorGradient: List<FeedXCtaColorGradient> = emptyList()
)

class FeedXCtaColorGradient(
    @SerializedName("color")
    val color: String = "",
    @SerializedName("position")
    val position: Double = 0.0
)

class FeedXCampaign(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName(
        "shortName"
    )
    val shortName: String = "",
    @SerializedName("startTime")
    val startTime: String = "",
    @SerializedName("endTime")
    val endTime: String = "",
    @SerializedName("restrictions")
    val restrictions: List<FeedXRestriction> = emptyList()
)

class FeedXRestriction(
    @SerializedName("isActive")
    val isActive: Boolean = false,
    @SerializedName("label")
    val label: String = ""
)

class FeedXPaginationInfo(
    @SerializedName("cursor")
    val cursor: String = "",
    @SerializedName("hasNext")
    val hasNext: Boolean = false,
    @SerializedName("totalData")
    val totalData: Int = 0
)

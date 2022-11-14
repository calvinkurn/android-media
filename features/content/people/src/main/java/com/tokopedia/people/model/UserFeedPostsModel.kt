package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class UserFeedPostsModel(
    @SerializedName("feedXProfileGetProfilePosts")
    val feedXProfileGetProfilePosts: FeedXProfileGetProfilePosts,
)

data class FeedXProfileGetProfilePosts(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("posts")
    val posts: List<Post>,
)

data class Pagination(
    @SerializedName("cursor")
    val cursor: String,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("totalData")
    val totalData: Int,
)

data class Post(
    @SerializedName("__typename")
    val __typename: String,
    @SerializedName("actionButtonLabel")
    val actionButtonLabel: String,
    @SerializedName("actionButtonOperationApp")
    val actionButtonOperationApp: String,
    @SerializedName("actionButtonOperationWeb")
    val actionButtonOperationWeb: String,
    @SerializedName("appLink")
    val appLink: String,
    @SerializedName("author")
    val author: Author,
    @SerializedName("comm")
    val comm: Comm,
    @SerializedName("deletable")
    val deletable: Boolean,
    @SerializedName("editable")
    val editable: Boolean,
    @SerializedName("fol")
    val fol: Fol,
    @SerializedName("hashtagAppLinkFmt")
    val hashtagAppLinkFmt: String,
    @SerializedName("hashtagWebLinkFmt")
    val hashtagWebLinkFmt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("like")
    val like: Like,
    @SerializedName("media")
    val media: List<Media>,
    @SerializedName("mediaRatio")
    val mediaRatio: MediaRatio,
    @SerializedName("mods")
    val mods: List<String>,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("reportable")
    val reportable: Boolean,
    @SerializedName("sh")
    val sh: Sh,
    @SerializedName("subTitle")
    val subTitle: String,
    @SerializedName("tags")
    val tags: List<Tag>,
    @SerializedName("text")
    val text: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("views")
    val views: Views,
    @SerializedName("webLink")
    val webLink: String,
): BaseItem()

data class Author(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("badgeURL")
    val badgeURL: String,
    @SerializedName("logoURL")
    val logoURL: String,
    @SerializedName("webLink")
    val webLink: String,
    @SerializedName("appLink")
    val appLink: String,
    @SerializedName("encryptedUserID")
    val encryptedUserID: String,
)

data class Comm(
    @SerializedName("count")
    val count: Int,
    @SerializedName("countFmt")
    val countFmt: String,
    @SerializedName("items")
    val items: List<CommList>,
    @SerializedName("label")
    val label: String,
    @SerializedName("mods")
    val mods: List<String>,
)

data class CommList(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: Author,
    @SerializedName("text")
    val text: String,
    @SerializedName("mods")
    val mods: List<String>,
)

data class Fol(
    @SerializedName("count")
    val count: Int,
    @SerializedName("countFmt")
    val countFmt: String,
    @SerializedName("isFollowed")
    val isFollowed: Boolean,
    @SerializedName("label")
    val label: String,
    @SerializedName("mods")
    val mods: List<String>,
)

data class Like(
    @SerializedName("count")
    val count: Int,
    @SerializedName("countFmt")
    val countFmt: String,
    @SerializedName("isLiked")
    val isLiked: Boolean,
    @SerializedName("label")
    val label: String,
    @SerializedName("likedBy")
    val likedBy: List<String>,
    @SerializedName("mods")
    val mods: List<String>,
)

data class Media(
    @SerializedName("appLink")
    val appLink: String,
    @SerializedName("coverURL")
    val coverURL: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("mediaURL")
    val mediaURL: String,
    @SerializedName("mods")
    val mods: List<String>,
    @SerializedName("tagging")
    val tagging: List<Tagging>,
    @SerializedName("type")
    val type: String,
    @SerializedName("webLink")
    val webLink: String,
)

data class MediaRatio(
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
)

data class Sh(
    @SerializedName("label")
    val label: String,
    @SerializedName("mods")
    val mods: List<String>,
    @SerializedName("operation")
    val operation: String,
)

data class Tag(
    @SerializedName("appLink")
    val appLink: String,
    @SerializedName("bebasOngkirStatus")
    val bebasOngkirStatus: String,
    @SerializedName("bebasOngkirURL")
    val bebasOngkirURL: String,
    @SerializedName("bestSellerFmt")
    val bestSellerFmt: String,
    @SerializedName("cashbackFmt")
    val cashbackFmt: String,
    @SerializedName("countReview")
    val countReview: Int,
    @SerializedName("countReviewFmt")
    val countReviewFmt: String,
    @SerializedName("coverURL")
    val coverURL: String,
    @SerializedName("discount")
    val discount: Int,
    @SerializedName("discountFmt")
    val discountFmt: String,
    @SerializedName("etaShipping")
    val etaShipping: String,
    @SerializedName("halalFmt")
    val halalFmt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("isBebasOngkir")
    val isBebasOngkir: Boolean,
    @SerializedName("isBestSeller")
    val isBestSeller: Boolean,
    @SerializedName("isCashback")
    val isCashback: Boolean,
    @SerializedName("isDiscount")
    val isDiscount: Boolean,
    @SerializedName("isHalal")
    val isHalal: Boolean,
    @SerializedName("isPricePerPiece")
    val isPricePerPiece: Boolean,
    @SerializedName("mods")
    val mods: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("priceDiscount")
    val priceDiscount: Double,
    @SerializedName("priceDiscountFmt")
    val priceDiscountFmt: String,
    @SerializedName("priceFmt")
    val priceFmt: String,
    @SerializedName("priceOriginal")
    val priceOriginal: Double,
    @SerializedName("priceOriginalFmt")
    val priceOriginalFmt: String,
    @SerializedName("pricePerPieceFmt")
    val pricePerPieceFmt: String,
    @SerializedName("shopBadgeURL")
    val shopBadgeURL: String,
    @SerializedName("shopID")
    val shopID: String,
    @SerializedName("shopName")
    val shopName: String,
    @SerializedName("shopType")
    val shopType: Int,
    @SerializedName("shopTypeName")
    val shopTypeName: String,
    @SerializedName("star")
    val star: Int,
    @SerializedName("totalSold")
    val totalSold: Int,
    @SerializedName("totalSoldFmt")
    val totalSoldFmt: String,
    @SerializedName("webLink")
    val webLink: String,
)

data class Tagging(
    @SerializedName("posX")
    val posX: Double,
    @SerializedName("posY:")
    val posY: Double,
    @SerializedName("tagIndex")
    val tagIndex: Int,
)

data class Views(
    @SerializedName("count")
    val count: Int,
    @SerializedName("countFmt")
    val countFmt: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("mods")
    val mods: List<String>,
)

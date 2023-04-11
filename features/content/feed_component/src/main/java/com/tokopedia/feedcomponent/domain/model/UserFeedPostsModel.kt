package com.tokopedia.feedcomponent.domain.model

import com.google.gson.annotations.SerializedName

data class UserFeedPostsModel(
    @SerializedName("feedXProfileGetProfilePosts")
    val feedXProfileGetProfilePosts: FeedXProfileGetProfilePosts = FeedXProfileGetProfilePosts(),
)

data class FeedXProfileGetProfilePosts(
    @SerializedName("pagination")
    val pagination: Pagination = Pagination(),
    @SerializedName("posts")
    val posts: List<Post> = emptyList(),
)

data class Pagination(
    @SerializedName("cursor")
    val cursor: String = "",
    @SerializedName("hasNext")
    val hasNext: Boolean = false,
    @SerializedName("totalData")
    val totalData: Int = 0,
)

data class Post(
    @SerializedName("__typename")
    val typename: String = "",
    @SerializedName("actionButtonLabel")
    val actionButtonLabel: String = "",
    @SerializedName("actionButtonOperationApp")
    val actionButtonOperationApp: String = "",
    @SerializedName("actionButtonOperationWeb")
    val actionButtonOperationWeb: String = "",
    @SerializedName("appLink")
    val appLink: String = "",
    @SerializedName("author")
    val author: Author = Author(),
    @SerializedName("comm")
    val comment: Comment = Comment(),
    @SerializedName("deletable")
    val deletable: Boolean = false,
    @SerializedName("editable")
    val editable: Boolean = false,
    @SerializedName("fol")
    val followers: Followers = Followers(),
    @SerializedName("hashtagAppLinkFmt")
    val hashtagAppLinkFmt: String = "",
    @SerializedName("hashtagWebLinkFmt")
    val hashtagWebLinkFmt: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("like")
    val like: Like = Like(),
    @SerializedName("media")
    val media: List<Media> = emptyList(),
    @SerializedName("mediaRatio")
    val mediaRatio: MediaRatio = MediaRatio(),
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("publishedAt")
    val publishedAt: String = "",
    @SerializedName("reportable")
    val reportable: Boolean = false,
    @SerializedName("sh")
    val share: Share = Share(),
    @SerializedName("subTitle")
    val subTitle: String = "",
    @SerializedName("tags")
    val tags: List<Tag> = emptyList(),
    @SerializedName("text")
    val text: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("views")
    val views: Views = Views(),
    @SerializedName("webLink")
    val webLink: String = "",
)

data class Author(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("badgeURL")
    val badgeURL: String = "",
    @SerializedName("logoURL")
    val logoURL: String = "",
    @SerializedName("webLink")
    val webLink: String = "",
    @SerializedName("appLink")
    val appLink: String = "",
)

data class Comment(
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("items")
    val items: List<CommentList> = emptyList(),
    @SerializedName("label")
    val label: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
)

data class CommentList(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("author")
    val author: Author = Author(),
    @SerializedName("text")
    val text: String = "",
)

data class Followers(
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("isFollowed")
    val isFollowed: Boolean = false,
    @SerializedName("label")
    val label: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
)

data class Like(
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("isLiked")
    val isLiked: Boolean = false,
    @SerializedName("label")
    val label: String = "",
    @SerializedName("likedBy")
    val likedBy: List<String> = emptyList(),
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
)

data class Media(
    @SerializedName("appLink")
    val appLink: String = "",
    @SerializedName("coverURL")
    val coverURL: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("mediaURL")
    val mediaURL: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("tagging")
    val tagging: List<Tagging> = emptyList(),
    @SerializedName("type")
    val type: String = "",
    @SerializedName("webLink")
    val webLink: String = "",
)

data class MediaRatio(
    @SerializedName("height")
    val height: Int = 0,
    @SerializedName("width")
    val width: Int = 0,
)

data class Share(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("operation")
    val operation: String = "",
)

data class Tag(
    @SerializedName("appLink")
    val appLink: String = "",
    @SerializedName("bebasOngkirStatus")
    val bebasOngkirStatus: String = "",
    @SerializedName("bebasOngkirURL")
    val bebasOngkirURL: String = "",
    @SerializedName("cashbackFmt")
    val cashbackFmt: String = "",
    @SerializedName("coverURL")
    val coverURL: String = "",
    @SerializedName("discount")
    val discount: Int = 0,
    @SerializedName("discountFmt")
    val discountFmt: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("isBebasOngkir")
    val isBebasOngkir: Boolean = false,
    @SerializedName("isCashback")
    val isCashback: Boolean = false,
    @SerializedName("isDiscount")
    val isDiscount: Boolean = false,
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price")
    val price: Double = 0.0,
    @SerializedName("priceDiscount")
    val priceDiscount: Double = 0.0,
    @SerializedName("priceDiscountFmt")
    val priceDiscountFmt: String = "",
    @SerializedName("priceFmt")
    val priceFmt: String = "",
    @SerializedName("priceOriginal")
    val priceOriginal: Double = 0.0,
    @SerializedName("priceOriginalFmt")
    val priceOriginalFmt: String = "",
    @SerializedName("shopID")
    val shopID: String = "",
    @SerializedName("shopName")
    val shopName: String = "",
    @SerializedName("star")
    val star: Int = 0,
    @SerializedName("totalSold")
    val totalSold: Int = 0,
    @SerializedName("webLink")
    val webLink: String = "",
)

data class Tagging(
    @SerializedName("posX")
    val posX: Float = 0F,
    @SerializedName("posY")
    val posY: Float = 0F,
    @SerializedName("tagIndex")
    val tagIndex: Int = 0,
)

data class Views(
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("countFmt")
    val countFmt: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("mods")
    val mods: List<String> = emptyList(),
)

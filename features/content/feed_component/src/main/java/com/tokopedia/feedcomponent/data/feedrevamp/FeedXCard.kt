package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXCard(
        @SerializedName("__typename")
        var typename: String,

        //FeedXCardBanners Data Type
        @SerializedName("id")
        var id: String,
        @SerializedName("publishedAt")
        var publishedAt: String,
        @SerializedName("mods")
        var mods: List<String>,

        //FeedXCardTopAds Data Type
        @SerializedName("promos")
        var promos: List<String>,
        @SerializedName("author")
        var author: FeedXAuthor,
        @SerializedName("items")
        var items: List<FeedXCardDataItem>,

        //FeedXCardPlaceHolder Data Type
        @SerializedName("type")
        var type: String,

        //FeedXCardProductsHighlight Data Type
        @SerializedName("products")
        var products: List<FeedXProduct>,
        @SerializedName("subTitle")
        var subTitle: String,
        @SerializedName("text")
        var text: String,
        @SerializedName("title")
        var title: String,
        @SerializedName("like")
        var like: FeedXLike,
        @SerializedName("comments")
        var comments: FeedXComments,
        @SerializedName("share")
        var share: FeedXShare,
        @SerializedName("followers")
        var followers: FeedXFollowers,

        //FeedXCardPost Data Type
        @SerializedName("appLink")
        var appLink: String,
        @SerializedName("webLink")
        var webLink: String,
        @SerializedName("actionButtonLabel")
        var actionButtonLabel: String,
        @SerializedName("actionButtonOperationWeb")
        var actionButtonOperationWeb: String,
        @SerializedName("actionButtonOperationApp")
        var actionButtonOperationApp: String,
        @SerializedName("media")
        var media: List<FeedXMedia>,
        @SerializedName("tags")
        var tags: List<FeedXProduct>,
        @SerializedName("hashtagAppLinkFmt")
        var hashtagAppLinkFmt: String,
        @SerializedName("hashtagWebLinkFmt")
        var hashtagWebLinkFmt: String,
)
package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXCard(
        @SerializedName("__typename")
        var typename: String = "",

        //FeedXCardBanners Data Type
        @SerializedName("id")
        var id: String = "",
        @SerializedName("publishedAt")
        var publishedAt: String = "",
        @SerializedName("reportable")
        var reportable:Boolean = true,
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
) {
    fun copyPostData(): FeedXCard {
        return FeedXCard(typename = typename, id = id, author = author, title = title, products = products, subTitle = subTitle, text = text,
                appLink = appLink, webLink = webLink, actionButtonLabel = actionButtonLabel,
                actionButtonOperationApp = actionButtonOperationApp, actionButtonOperationWeb = actionButtonOperationWeb,
                media = media, tags = tags, hashtagAppLinkFmt = hashtagAppLinkFmt, hashtagWebLinkFmt = hashtagWebLinkFmt,
                like = like, comments = comments, share = share, followers = followers, publishedAt = publishedAt,
                mods = mods)
    }
}
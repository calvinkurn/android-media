package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/06/20.
 */
data class GetChannelResponse(
    @SerializedName("broadcasterGetChannels")
    val broadcasterGetChannels: GetChannels = GetChannels()
) {

    data class GetChannels(
            @SerializedName("channels")
            val channels: List<Channel> = listOf()
    )

    data class Channel(
            @SerializedName("basic")
            val basic: ChannelBasic = ChannelBasic(),
            @SerializedName("author")
            val author: Author = Author(),
            @SerializedName("medias")
            val medias: List<Media> = listOf(),
            @SerializedName("productTags")
            val productTags: List<ProductTag> = listOf(),
            @SerializedName("pinMessages")
            val pinMessages: List<QuickReply> = listOf(),
            @SerializedName("quickReplies")
            val quickReplies: List<QuickReply> = listOf(),
            @SerializedName("publicVouchers")
            val publicVouchers: List<PublicVoucher> = listOf(),
            @SerializedName("share")
            val share: Share = Share()
    )

    data class ChannelBasic(
            @SerializedName("channelID")
            val channelId: String = "",
            @SerializedName("coverURL")
            val coverUrl: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("slug")
            val slug: String = "",
            @SerializedName("description")
            val description: String = "",
            @SerializedName("activeMediaID")
            val activeMediaID: String = "",
            @SerializedName("startTime")
            val startTime: String = "",
            @SerializedName("endTime")
            val endTime: String = "",
            @SerializedName("enableChat")
            val enableChat: Boolean = false,
            @SerializedName("status")
            val status: ChannelBasicStatus = ChannelBasicStatus(),
            @SerializedName("timestamp")
            val timestamp: Timestamp = Timestamp()
    )

    data class ChannelBasicStatus(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("text")
            val text: String = ""
    )

    data class Timestamp(
            @SerializedName("publishedAt")
            val publishedAt: String = ""
    )

    data class Author(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("thumbnailURL")
            val thumbnailUrl: String = "",
            @SerializedName("badge")
            val badge: String = ""
    )

    data class Media(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("channelID")
            val channelId: String = "",
            @SerializedName("coverURL")
            val coverUrl: String = "",
            @SerializedName("source")
            val source: String = "",
            @SerializedName("ingestURL")
            val ingestUrl: String = "",
            @SerializedName("livestreamID")
            val liveStreamId: String = ""
    )

    data class ProductTag(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("channelID")
            val channelId: String = "",
            @SerializedName("productID")
            val productID: String = "",
            @SerializedName("weight")
            val weight: Long = 0,
            @SerializedName("productName")
            val productName: String = "",
            @SerializedName("description")
            val description: String = "",
            @SerializedName("originalPriceFmt")
            val originalPriceFmt: String = "",
            @SerializedName("originalPrice")
            val originalPrice: String = "",
            @SerializedName("priceFmt")
            val priceFmt: String = "",
            @SerializedName("price")
            val price: String = "",
            @SerializedName("discount")
            val discount: String = "",
            @SerializedName("quantity")
            val quantity: Int = 0,
            @SerializedName("isVariant")
            val isVariant: Boolean = false,
            @SerializedName("isAvailable")
            val isAvailable: Boolean = false,
            @SerializedName("order")
            val order: Int = 0,
            @SerializedName("applink")
            val applink: String = "",
            @SerializedName("weblink")
            val weblink: String = "",
            @SerializedName("minQuantity")
            val minQuantity: Int = 0,
            @SerializedName("imageURL")
            val imageUrl: String = "",
            @SerializedName("isFreeShipping")
            val isFreeShipping: Boolean = false
    )

    data class PinnedMessage(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("channelID")
            val channelId: String = "",
            @SerializedName("message")
            val message: String = "",
            @SerializedName("appLink")
            val appLink: String = "",
            @SerializedName("webLink")
            val webLink: String = "",
            @SerializedName("imageURL")
            val imageURL: String = "",
            @SerializedName("weight")
            val weight: Long = 0
    )

    data class QuickReply(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("channelID")
            val channelId: String = "",
            @SerializedName("message")
            val message: String = "",
            @SerializedName("weight")
            val weight: Long = 0
    )

    data class PublicVoucher(
            @SerializedName("ID")
            val id: String = "",
            @SerializedName("ShopID")
            val shopId: String = "",
            @SerializedName("Name")
            val name: String = "",
            @SerializedName("Title")
            val title: String = "",
            @SerializedName("Subtitle")
            val subtitle: String = "",
            @SerializedName("Type")
            val type: Int = 0,
            @SerializedName("Image")
            val image: String = "",
            @SerializedName("ImageSquare")
            val imageSquare: String = "",
            @SerializedName("Quota")
            val quota: Int = 0,
            @SerializedName("FinishTime")
            val finishTime: String = "",
            @SerializedName("Code")
            val code: String = "",
            @SerializedName("QuotaAvailable")
            val quotaAvailable: Int = 0,
            @SerializedName("UsedQuota")
            val usedQuota: Int = 0,
            @SerializedName("TnC")
            val tnC: String = ""
    )

    data class Share(
            @SerializedName("text")
            val text: String = "",
            @SerializedName("redirectURL")
            val redirectURL: String = "",
            @SerializedName("useShortURL")
            val useShortURL: Boolean = false,
            @SerializedName("metaTitle")
            val metaTitle: String = "",
            @SerializedName("metaDescription")
            val metaDescription: String = ""
    )
}
package com.tokopedia.v2.home.model.pojo.home

import com.google.gson.annotations.SerializedName

data class PlayCardHome(
    @SerializedName("playGetCardHome")
    val playGetCardHome: PlayGetCardHome
)

data class PlayGetCardHome(
    @SerializedName("data")
    val data: Data = Data()
)

data class Data(
    @SerializedName("card")
    val card: PlayCard = PlayCard()
)

data class PlayCard(
    @SerializedName("card_id")
    val cardId: Int = -1,
    @SerializedName("broadcaster_type")
    val broadcasterType: String = "",
    @SerializedName("broadcaster_id")
    val broadcasterId: Int = -1,
    @SerializedName("broadcaster_name")
    val broadcasterName: String = "",
    @SerializedName("broadcaster_image")
    val broadcasterImage: String = "",
    @SerializedName("broadcaster_badge_type")
    val broadcasterBadgeType: String = "",
    @SerializedName("broadcaster_app_link")
    val broadcasterAppLink: String = "",
    @SerializedName("broadcaster_web_link")
    val broadcasterWebLink: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("app_link")
    val applink: String = "",
    @SerializedName("is_show_live")
    val isShowLive: Boolean = false,
    @SerializedName("is_show_total_view")
    val isShowTotalView: Boolean = false,
    @SerializedName("is_lite")
    val isLite: Boolean = false,
    @SerializedName("campaign_name")
    val campaignName: String = "",
    @SerializedName("total_view")
    val totalView: String = ""
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayCard

        if (cardId != other.cardId) return false
        if (broadcasterType != other.broadcasterType) return false
        if (broadcasterId != other.broadcasterId) return false
        if (broadcasterName != other.broadcasterName) return false
        if (broadcasterImage != other.broadcasterImage) return false
        if (broadcasterBadgeType != other.broadcasterBadgeType) return false
        if (broadcasterAppLink != other.broadcasterAppLink) return false
        if (broadcasterWebLink != other.broadcasterWebLink) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (imageUrl != other.imageUrl) return false
        if (applink != other.applink) return false
        if (isShowLive != other.isShowLive) return false
        if (isShowTotalView != other.isShowTotalView) return false
        if (isLite != other.isLite) return false
        if (campaignName != other.campaignName) return false
        if (totalView != other.totalView) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cardId
        result = 31 * result + broadcasterType.hashCode()
        result = 31 * result + broadcasterId
        result = 31 * result + broadcasterName.hashCode()
        result = 31 * result + broadcasterImage.hashCode()
        result = 31 * result + broadcasterBadgeType.hashCode()
        result = 31 * result + broadcasterAppLink.hashCode()
        result = 31 * result + broadcasterWebLink.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + applink.hashCode()
        result = 31 * result + isShowLive.hashCode()
        result = 31 * result + isShowTotalView.hashCode()
        result = 31 * result + isLite.hashCode()
        result = 31 * result + campaignName.hashCode()
        result = 31 * result + totalView.hashCode()
        return result
    }
}
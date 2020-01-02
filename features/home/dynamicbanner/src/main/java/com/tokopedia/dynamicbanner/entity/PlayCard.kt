package com.tokopedia.dynamicbanner.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayCard(
        @Expose @SerializedName("card_id") var cardId: Int = 0,
        @Expose @SerializedName("broadcaster_type") var broadcasterType: String = "",
        @Expose @SerializedName("broadcaster_id") var broadcasterId: Int = 0,
        @Expose @SerializedName("broadcaster_name") var broadcasterName: String = "",
        @Expose @SerializedName("broadcaster_image") var broadcasterImage: String = "",
        @Expose @SerializedName("broadcaster_badge_type") var broadcasterBadgeType: String = "",
        @Expose @SerializedName("broadcaster_app_link") var broadcasterAppLink: String = "",
        @Expose @SerializedName("broadcaster_web_link") var broadcasterWebLink: String = "",
        @Expose @SerializedName("title") var title: String = "",
        @Expose @SerializedName("description") var description: String = "",
        @Expose @SerializedName("image_url") var imageUrl: String = "",
        @Expose @SerializedName("app_link") var applink: String = "",
        @Expose @SerializedName("web_link") var weblink: String = "",
        @Expose @SerializedName("is_show_live") var isShowLive: Boolean = false,
        @Expose @SerializedName("is_show_total_view") var isShowTotalView: Boolean = false,
        @Expose @SerializedName("is_lite") var isLite: Boolean = false,
        @Expose @SerializedName("og_title") var ogTitle: String = "",
        @Expose @SerializedName("og_description") var ogDescription: String = "",
        @Expose @SerializedName("og_url") var ogUrl: String = "",
        @Expose @SerializedName("og_image") var ogImage: String = "",
        @Expose @SerializedName("start_date") var startDate: String = "",
        @Expose @SerializedName("end_date") var endDate: String = "",
        @Expose @SerializedName("created_date") var createdDate: String = "",
        @Expose @SerializedName("updated_date") var updatedDate: String = "",
        @Expose @SerializedName("campaign_name") var campaignName: String = "",
        @Expose @SerializedName("total_view") var totalView: String = "0",
        @Expose @SerializedName("livestream_id") var livestreamId: String = "0",
        @Expose @SerializedName("channel_id") var channelId: String = "0"

)
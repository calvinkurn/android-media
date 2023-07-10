package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName

// pinned_product: [PlayPinnedProduct]
// pinned_voucher: [PlayPinnedVoucher]
// pinned_message: PlayPinnedMessage
// configurations: PlayChannelConfig
// stats: PlayChannelStats
// Engagement statistics summary

// share: PlayChannelShare

data class PlaySlotItems(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("start_time")
    val start_time: String = "",
    @SerializedName("end_time")
    val end_time: String = "",
    @SerializedName("is_live")
    val is_live: Boolean = true,
    @SerializedName("air_time")
    val air_time: String = "",
    @SerializedName("cover_url")
    val cover_url: String = "",
    @SerializedName("partner")
    val partner: PlayPartner = PlayPartner(),
    @SerializedName("video")
    val video: PlayVideoStream = PlayVideoStream(),
    @SerializedName("stats")
    val stats: PlayChannelStats = PlayChannelStats(),
    @SerializedName("configurations")
    val configurations: Configurations,
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("web_link")
    val webLink: String = "",

    @SerializedName("share")
    val share: PlayChannelShare = PlayChannelShare(),
    @SerializedName("display_type")
    val display_type: String = "",
    @SerializedName("recommendationType")
    val recommendationType: String = "",

    // PlayBanner
    @SerializedName("created_time")
    val created_time: String = "",
    @SerializedName("updated_time")
    val updated_time: String = "",
    @SerializedName("slug")
    val slug: String = "",
    @SerializedName("broadcaster_name")
    val broadcaster_name: String = "",
    @SerializedName("image_url")
    val image_url: String? = "",

    // PlaySlotTabMenu
    @SerializedName("label")
    val label: String = "",
    @SerializedName("icon_url")
    val icon_url: String = "",
    @SerializedName("group")
    val group: String = "",
    @SerializedName("source_type")
    val source_type: String = "",
    @SerializedName("source_id")
    val source_id: String = "",
    @SerializedName("slug_id")
    val slug_id: String = ""
)

data class PlayChannelStats(
    @SerializedName("view")
    val view: PlayChannelViewStats = PlayChannelViewStats()
)

data class PlayChannelViewStats(
    @SerializedName("value")
    val value: String = "",
    @SerializedName("formatted")
    val formatted: String = ""
)

data class Configurations(
    @SerializedName("has_promo")
    val hasPromo: Boolean,
    @SerializedName("promo_labels")
    val promoLabels: List<PromoLabel>,
    @SerializedName("reminder")
    val reminder: Reminder
) {
    data class PromoLabel(
        @SerializedName("text")
        val text: String,
        @SerializedName("type")
        val type: String
    )

    data class Reminder(
        @SerializedName("is_set")
        val isSet: Boolean
    )
}

package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.widget.sample.data.PlayPartner
import com.tokopedia.play.widget.sample.data.PlayPinnedMessage
import com.tokopedia.play.widget.sample.data.PlayPinnedVoucher


//pinned_product: [PlayPinnedProduct]
//pinned_voucher: [PlayPinnedVoucher]
//pinned_message: PlayPinnedMessage
//configurations: PlayChannelConfig
//stats: PlayChannelStats
//Engagement statistics summary

//share: PlayChannelShare


data class PlaySlotItems(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("start_time")
        var start_time: String = "",
        @SerializedName("end_time")
        var end_time: String = "",
        @SerializedName("is_live")
        var is_live: Boolean = true,
        @SerializedName("air_time")
        var air_time: String = "",
        @SerializedName("cover_url")
        var cover_url: String = "",
        @SerializedName("video")
        var video: PlayVideoStream = PlayVideoStream(),
        @SerializedName("pinned_product")
        var pinned_product: List<PlayPinnedProduct> = emptyList(),
        @SerializedName("pinned_voucher")
        var pinned_voucher: List<PlayPinnedVoucher> = emptyList(),
        @SerializedName("pinned_message")
        var pinned_message: PlayPinnedMessage = PlayPinnedMessage(),
        @SerializedName("partner")
        var partner: PlayPartner = PlayPartner(),
        @SerializedName("quick_replies")
        var quick_replies: List<String> = emptyList(),
        @SerializedName("configurations")
        val configurations:Configurations,
        @SerializedName("stats")
        var stats: PlayChannelStats = PlayChannelStats(),
        @SerializedName("app_link")
        var appLink: String = "",
        @SerializedName("web_link")
        var webLink: String = "",

        @SerializedName("share")
        var share: PlayChannelShare = PlayChannelShare(),
        @SerializedName("display_type")
        var display_type: String = "",
        @SerializedName("recommendationType")
        var recommendationType: String = "",

        //PlayBanner
        @SerializedName("created_time")
        var created_time: String = "",
        @SerializedName("updated_time")
        var updated_time: String = "",
        @SerializedName("slug")
        var slug: String = "",
        @SerializedName("broadcaster_name")
        var broadcaster_name: String = "",
        @SerializedName("image_url")
        var image_url: String? = "",

        //PlaySlotTabMenu
        @SerializedName("label")
        var label: String = "",
        @SerializedName("icon_url")
        var icon_url: String = "",
        @SerializedName("group")
        var group: String = "",
        @SerializedName("source_type")
        var source_type: String = "",
        @SerializedName("source_id")
        var source_id: String = "",
        @SerializedName("slug_id")
        var slug_id: String = "",


        )

        data class PlayChannelStats(
                @SerializedName("view")
                var view: PlayChannelViewStats = PlayChannelViewStats(),
        )

        data class PlayChannelViewStats(
                @SerializedName("value")
                var value: String = "",
                @SerializedName("formatted")
                var formatted: String = "",
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


package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlaySlot(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("items")
        var mods: List<PlaySlotItems> = emptyList(),
        @SerializedName("lihat_semua")
        var lihat_semua: PlayLihatSemua = PlayLihatSemua(),
        @SerializedName("inplace_pager")
        var inplace_pager: PlaySlotParams = PlaySlotParams(),
        @SerializedName("hash")
        var hash: String = "",
        @SerializedName(" is_autoplay")
        var  is_autoplay: Boolean = false,
)

data class PlayLihatSemua(
        @SerializedName("show")
        var show: Boolean = false,
        @SerializedName("label")
        var label: String = "",
        @SerializedName("web_link")
        var web_link: String = ""
)
data class PlaySlotParams(
        @SerializedName("group")
        var group: String = "",
        @SerializedName("cursor")
        var cursor: String = "",
        @SerializedName("source_type")
        var source_type: String = "",
        @SerializedName("source_id")
        var source_id: String = "",
)

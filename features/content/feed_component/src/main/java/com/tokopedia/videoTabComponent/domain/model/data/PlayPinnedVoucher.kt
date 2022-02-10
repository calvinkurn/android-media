package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlayPinnedVoucher(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("shop_id")
        var shop_id: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("subtitle")
        var subtitle: String = "",
        @SerializedName("image_url")
        var image_url: String = "",
        @SerializedName("image_square_url")
        var image_square_url: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("code")
        var code: String = "",
        @SerializedName("quota")
        var quota: Int = 0,
        @SerializedName("quota_available")
        var quota_available: Int = 0,
        @SerializedName("finish_time")
        var finish_time: Boolean = false,
        @SerializedName("tnc")
        var tnc: String = "",




)

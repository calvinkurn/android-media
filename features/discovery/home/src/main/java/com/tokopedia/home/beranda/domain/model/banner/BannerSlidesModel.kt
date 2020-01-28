package com.tokopedia.home.beranda.domain.model.banner

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class BannerSlidesModel(
        @SerializedName("id")
        val id: Int = -1,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("image_url")
        var imageUrl: String = "",
        @SerializedName("redirect_url")
        val redirectUrl: String = "",
        @SerializedName("creative_name")
        val creativeName: String = "",
        @SerializedName("applink")
        var applink: String = "",
        @SerializedName("target")
        val target: Int = 0,
        @SerializedName("device")
        val device: Int = 0,
        @SerializedName("expire_time")
        val expireTime: String = "",
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("created_by")
        val createdBy: Int = 0,
        @SerializedName("created_on")
        val createdOn: String = "",
        @SerializedName("updated_by")
        val updatedBy: Int = 0,
        @SerializedName("updated_on")
        val updatedOn: String = "",
        @SerializedName("start_time")
        val startTime: String = "",
        @SerializedName("slide_index")
        val slideIndex: Int = 0,
        @SerializedName("promo_code")
        val promoCode: String = "",
        @SerializedName("topads_view_url")
        val topadsViewUrl: String = "",
        @SerializedName("type")
        var type: String = ""
): ImpressHolder() {

    var isImpressed = false
    var position = -1
    companion object{
        const val TYPE_BANNER_PERSO = "overlay"
        const val TYPE_BANNER_DEFAULT = "default"
    }
}
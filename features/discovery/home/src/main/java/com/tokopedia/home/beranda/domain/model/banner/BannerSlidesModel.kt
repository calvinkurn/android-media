package com.tokopedia.home.beranda.domain.model.banner

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class BannerSlidesModel(
        @SerializedName("id")
        val id: Int = -1,
        @SerializedName("galaxy_attribution")
        val galaxyAttribution: String = "",
        @SerializedName("persona")
        val persona: String = "",
        @SerializedName("brand_id")
        val brandId: String = "",
        @SerializedName("category_persona")
        val categoryPersona: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("image_url")
        var imageUrl: String = "",
        @SerializedName("redirect_url")
        val redirectUrl: String = "",
        @SerializedName("creative_name")
        val creativeName: String = "",
        @SerializedName("applink")
        var applink: String = "",
        @SerializedName("promo_code")
        val promoCode: String = "",
        @SerializedName("topads_view_url")
        val topadsViewUrl: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("category_id")
        var categoryId: String = "",
        @SerializedName("campaignCode")
        val campaignCode: String = "",
        var position: Int = -1
): ImpressHolder() {

    companion object{
        const val TYPE_BANNER_PERSO = "overlay"
        const val TYPE_BANNER_DEFAULT = "default"
    }
}
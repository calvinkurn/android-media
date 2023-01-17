package com.tokopedia.home.beranda.domain.model.banner

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class BannerSlidesModel(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        val id: Int = -1,
        @SerializedName("galaxy_attribution", alternate = ["galaxyAttribution"])
        val galaxyAttribution: String = "",
        @SerializedName("persona")
        val persona: String = "",
        @SerializedName("brand_id", alternate = ["brandID"])
        val brandId: String = "",
        @SerializedName("category_persona", alternate = ["categoryPersona"])
        val categoryPersona: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("image_url", alternate = ["imageUrl"])
        var imageUrl: String = "",
        @SerializedName("redirect_url", alternate = ["url"])
        val redirectUrl: String = "",
        @SerializedName("creative_name", alternate = ["creativeName"])
        val creativeName: String = "",
        @SerializedName("applink")
        var applink: String = "",
        @SerializedName("promo_code", alternate = ["promoCode"])
        val promoCode: String = "",
        @SerializedName("topads_view_url", alternate = ["topadsViewUrl"])
        val topadsViewUrl: String = "",
        @SerializedName("type")
        var type: String = "",
        @SerializedName("category_id", alternate = ["categoryID"])
        var categoryId: String = "",
        @SerializedName("campaignCode")
        val campaignCode: String = "",
        var position: Int = -1,
): ImpressHolder() {

    companion object{
        const val TYPE_BANNER_PERSO = "overlay"
        const val TYPE_BANNER_DEFAULT = "default"
    }
}

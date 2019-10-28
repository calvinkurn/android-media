package com.tokopedia.promocheckout.detail.model.couponredeem

import com.google.gson.annotations.SerializedName

data class CouponsItem(

        @SerializedName("owner")
        val owner: Int? = null,

        @SerializedName("cta")
        val cta: String? = null,

        @SerializedName("code")
        val code: String? = null,

        @SerializedName("cta_desktop")
        val ctaDesktop: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("promo_id")
        val promoId: Int? = null,

        @SerializedName("title")
        val title: String? = null
)

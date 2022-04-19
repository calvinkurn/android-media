package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName


data class TrackingFields(
        @SerializedName("bu")
        var bu: String? = "",

        @SerializedName("category_id")
        val categoryId: String? = "",

        @SerializedName("persona")
        val persona: String? = "",

        @SerializedName("promo_code")
        val promoCode: String? = "",

        @SerializedName("promo_id")
        val promoId: String? = "",

        @SerializedName("shop_domain")
        val shopDomain: String? = ""
)
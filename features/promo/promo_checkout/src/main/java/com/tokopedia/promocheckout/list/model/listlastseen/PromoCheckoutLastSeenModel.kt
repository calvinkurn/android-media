package com.tokopedia.promocheckout.list.model.listlastseen

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 11/07/19.
 */
class PromoCheckoutLastSeenModel(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("title")
        @Expose
        val title: String,
        @SerializedName("subtitle")
        @Expose
        val subtitle: String,
        @SerializedName("promo_code")
        @Expose
        val promoCode: String
) {
        class Response(
                @SerializedName("rechargePromoBanner")
                @Expose
                var promoModels: List<PromoCheckoutLastSeenModel> = listOf()
        )
}
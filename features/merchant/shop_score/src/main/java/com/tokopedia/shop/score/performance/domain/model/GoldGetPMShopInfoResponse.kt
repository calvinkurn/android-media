package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldGetPMShopInfoResponse(
        @Expose
        @SerializedName("goldGetPMShopInfo")
        val goldGetPMShopInfo: GoldGetPMShopInfo = GoldGetPMShopInfo()
) {
        data class GoldGetPMShopInfo(
                @Expose
                @SerializedName("is_eligible_pm")
                val isEligiblePm: Boolean = false,
                @Expose
                @SerializedName("is_new_seller")
                val isNewSeller: Boolean = false
        )
}
package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoIsNewSellerResponse(
        @Expose
        @SerializedName("goldGetPMShopInfo")
        val goldGetPMShopInfo: GoldGetPMShopInfo = GoldGetPMShopInfo()
) {
    data class GoldGetPMShopInfo(
            @Expose
            @SerializedName("data")
            val `data`: Data = Data()
    ) {
        data class Data(
                @Expose
                @SerializedName("is_new_seller")
                val isNewSeller: Boolean = false
        )
    }
}
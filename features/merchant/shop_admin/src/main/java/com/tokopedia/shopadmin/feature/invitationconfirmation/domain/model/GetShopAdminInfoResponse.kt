package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopAdminInfoResponse(
    @SerializedName("shop")
    @Expose
    val shop: Shop = Shop()
) {
    data class Shop(
        @SerializedName("logo")
        @Expose
        val logo: String = "",
        @SerializedName("shop_name")
        @Expose
        val shopName: String = ""
    )
}
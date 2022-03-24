package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopAdminInfoResponse(
    @SerializedName("shop")
    @Expose
    val shop: Shop = Shop(),
    @SerializedName("getAdminInfo")
    @Expose
    val getAdminInfo: GetAdminInfo = GetAdminInfo()
) {
    data class Shop(
        @SerializedName("logo")
        @Expose
        val logo: String = "",
        @SerializedName("shop_name")
        @Expose
        val shopName: String = ""
    )

    data class GetAdminInfo(
        @SerializedName("admin_data")
        @Expose
        val adminData: List<AdminData> = listOf()
    ) {
        data class AdminData(
            @SerializedName("shop_manage_id")
            @Expose
            val shopManageId: String = ""
        )
    }
}
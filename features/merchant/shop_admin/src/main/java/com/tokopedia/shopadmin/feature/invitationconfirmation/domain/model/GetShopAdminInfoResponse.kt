package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetShopAdminInfoResponse(
    @SerializedName("shopInfoByID")
    @Expose
    val shopInfoByID: ShopInfoByID = ShopInfoByID(),
    @SerializedName("getAdminInfo")
    @Expose
    val getAdminInfo: GetAdminInfo = GetAdminInfo()
) {

    data class ShopInfoByID(
        @SerializedName("result")
        @Expose
        val result: List<Result> = listOf()
    ) {
        data class Result(
            @SerializedName("shopAssets")
            @Expose
            val shopAssets: ShopAssets = ShopAssets(),
            @SerializedName("shopCore")
            @Expose
            val shopCore: ShopCore = ShopCore()
        ) {
            data class ShopAssets(
                @SerializedName("avatar")
                @Expose
                val avatar: String = ""
            )

            data class ShopCore(
                @SerializedName("name")
                @Expose
                val name: String = ""
            )
        }
    }

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
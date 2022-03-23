package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetAdminInfoResponse(
    @SerializedName("getAdminType")
    @Expose
    val getAdminType: GetAdminType = GetAdminType(),
    @SerializedName("getAdminInfo")
    @Expose
    val getAdminInfo: GetAdminInfo = GetAdminInfo()
) {
    data class GetAdminType(
        @SerializedName("admin_data")
        @Expose
        val adminData: AdminData = AdminData(),
        @SerializedName("shopID")
        @Expose
        val shopID: String = ""
    ) {
        data class AdminData(
            @SerializedName("status")
            @Expose
            val status: String = ""
        )
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
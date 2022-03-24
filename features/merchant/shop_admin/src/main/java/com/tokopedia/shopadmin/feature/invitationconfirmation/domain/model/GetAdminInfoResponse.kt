package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetAdminInfoResponse(
    @SerializedName("getAdminType")
    @Expose
    val getAdminType: GetAdminType = GetAdminType()
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
}
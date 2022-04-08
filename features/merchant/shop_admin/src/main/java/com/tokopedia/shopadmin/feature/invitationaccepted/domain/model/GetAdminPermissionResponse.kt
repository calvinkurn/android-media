package com.tokopedia.shopadmin.feature.invitationaccepted.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAdminPermissionResponse(
    @SerializedName("getAdminInfo")
    @Expose
    val getAdminInfo: GetAdminInfo = GetAdminInfo()
) {
    data class GetAdminInfo(
        @SerializedName("admin_data")
        @Expose
        val adminData: List<AdminData> = listOf()
    ) {
        data class AdminData(
            @SerializedName("permission_list")
            @Expose
            val permissionList: List<Permission> = listOf()
        ) {
            data class Permission(
                @SerializedName("permission_name")
                @Expose
                val permissionName: String = "",
                @SerializedName("icon_url")
                @Expose
                val iconUrl: String = ""
            )
        }
    }
}
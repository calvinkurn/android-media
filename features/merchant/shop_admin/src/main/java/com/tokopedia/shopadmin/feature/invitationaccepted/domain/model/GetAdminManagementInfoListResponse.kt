package com.tokopedia.shopadmin.feature.invitationaccepted.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetAdminManagementInfoListResponse(
    @SerializedName("getAdminManagementInfoList")
    @Expose
    val getAdminManagementInfoList: GetAdminManagementInfoList = GetAdminManagementInfoList()
) {
    data class GetAdminManagementInfoList(
        @SerializedName("allPermissionList")
        @Expose
        val allPermissionList: List<AllPermission> = listOf()
    ) {
        data class AllPermission(
            @SerializedName("iconURL")
            @Expose
            val iconURL: String = "",
            @SerializedName("permissionName")
            @Expose
            val permissionName: String = "",
            @SerializedName("resourcetype")
            @Expose
            val resourceType: String = ""
        )
    }
}
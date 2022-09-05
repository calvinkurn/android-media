package com.tokopedia.shopadmin.feature.invitationaccepted.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
            @SerializedName("description")
            @Expose
            val description: String = "",
            @SerializedName("iconURL")
            @Expose
            val iconURL: String = "",
            @SerializedName("permissionId")
            @Expose
            val permissionId: String = "",
            @SerializedName("permissionName")
            @Expose
            val permissionName: String = "",
            @SerializedName("permissionRecursive")
            @Expose
            val permissionRecursive: List<PermissionRecursive> = listOf()
        ) {
            data class PermissionRecursive(
                @SerializedName("description")
                @Expose
                val description: String = "",
                @SerializedName("iconURL")
                @Expose
                val iconURL: String = "",
                @SerializedName("permissionId")
                @Expose
                val permissionId: String = "",
                @SerializedName("permissionName")
                @Expose
                val permissionName: String = ""
            )
        }
    }
}
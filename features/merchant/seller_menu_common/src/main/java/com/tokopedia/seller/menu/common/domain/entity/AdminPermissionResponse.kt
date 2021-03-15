package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.SerializedName

data class AdminPermissionResponse(
        @SerializedName("getAdminType")
        val adminPermissionData: AdminPermissionData? = AdminPermissionData()
)

data class AdminPermissionData(
        @SerializedName("admin_data")
        val adminData: AdminData? = AdminData(),
        @SerializedName("response_detail")
        val responseDetail: AdminResponseDetail? = AdminResponseDetail()
)

data class AdminData(
        @SerializedName("permission_list")
        val permissionList: List<AdminPermission>? = listOf()
)

data class AdminResponseDetail(
        @SerializedName("error_message")
        val errorMessage: String? = ""
)

data class AdminPermission(
        @SerializedName("permission_id")
        val permissionId: String? = ""
)
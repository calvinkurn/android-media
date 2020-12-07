package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminPermissionResponse(
        @SerializedName("getAdminInfo")
        @Expose
        val adminInfo: GetAdminPermission? = GetAdminPermission()
)

data class GetAdminPermission(
        @SerializedName("admin_data")
        @Expose
        val adminData: List<AdminPermissionData>? = listOf()
)

data class AdminPermissionData(
        @SerializedName("permission_list")
        @Expose
        val permissionList: List<AdminPermission>? = listOf(),
        @SerializedName("response_detail")
        @Expose
        val responseDetail: AdminResponseDetail? = AdminResponseDetail()
)
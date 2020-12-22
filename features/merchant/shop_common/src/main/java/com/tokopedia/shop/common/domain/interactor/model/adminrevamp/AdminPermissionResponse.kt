package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminPermissionResponse(
        @SerializedName("getAdminType")
        @Expose
        val adminInfo: GetAdminPermission? = GetAdminPermission()
)

data class GetAdminPermission(
        @SerializedName("admin_data")
        @Expose
        val adminData: AdminPermissionData = AdminPermissionData(),
        @SerializedName("response_detail")
        @Expose
        val responseDetail: AdminResponseDetail? = AdminResponseDetail()
)

data class AdminPermissionData(
        @SerializedName("permission_list")
        @Expose
        val permissionList: List<AdminPermission>? = listOf()
)
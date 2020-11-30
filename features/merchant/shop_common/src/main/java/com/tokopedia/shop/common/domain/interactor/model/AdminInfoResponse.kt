package com.tokopedia.shop.common.domain.interactor.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminInfoResponse(
        @SerializedName("getAdminInfo")
        @Expose
        val adminInfo: AdminInfo? = AdminInfo()
)

data class AdminInfo(
        @SerializedName("admin_data")
        @Expose
        val adminData: AdminInfoData? = AdminInfoData()
)

data class AdminInfoData(
        @SerializedName("permission_list")
        @Expose
        val permissionList: List<AdminInfoPermission>? = listOf(),
        @SerializedName("detail_information")
        @Expose
        val detailInfo: AdminInfoDetailInformation? = AdminInfoDetailInformation(),
        @SerializedName("response_detail")
        @Expose
        val responseDetail: AdminInfoResponseDetail? = AdminInfoResponseDetail()
)

sealed class AdminInfoResult {
        class Success(val data: AdminInfoData): AdminInfoResult()
        class Fail(val throwable: Exception): AdminInfoResult()
}

data class AdminInfoPermissionList(
        @SerializedName("permission_list")
        @Expose
        val permissionList: List<AdminInfoPermission>? = listOf()
)

data class AdminInfoDetailInformation(
        @SerializedName("admin_role_type")
        @Expose
        val adminRoleType: AdminRoleType? = AdminRoleType()
)

data class AdminRoleType(
        @SerializedName("is_shop_admin")
        @Expose
        val isShopAdmin: Boolean? = false,
        @SerializedName("is_location_admin")
        @Expose
        val isLocationAdmin: Boolean? = false,
        @SerializedName("is_shop_owner")
        @Expose
        val isShopOwner: Boolean? = false
)

data class AdminInfoPermission(
        @SerializedName("permission_id")
        @Expose
        val id: String? = "",
        @SerializedName("permission_name")
        @Expose
        val name: String? = ""
)

data class AdminInfoResponseDetail(
        @SerializedName("code")
        @Expose
        val code: Int? = 0,
        @SerializedName("error_message")
        @Expose
        val errorMessage: String? = ""
)
package com.tokopedia.shop.common.domain.interactor.model.adminrevamp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminInfoResponse(
        @SerializedName("getAdminInfo")
        @Expose
        val adminInfo: GetAdminInfo? = GetAdminInfo()
)

data class GetAdminInfo(
        @SerializedName("admin_data")
        @Expose
        val adminData: List<AdminInfoData>? = listOf()
)

data class AdminInfoData(
        @SerializedName("permission_list")
        @Expose
        val permissionList: List<AdminPermission>? = listOf(),
        @SerializedName("detail_information")
        @Expose
        val detailInfo: AdminInfoDetailInformation? = AdminInfoDetailInformation(),
        @SerializedName("response_detail")
        @Expose
        val responseDetail: AdminResponseDetail? = AdminResponseDetail()
)

sealed class AdminInfoResult {
        class Success(val data: AdminInfoData?): AdminInfoResult()
        class Fail(val throwable: Exception): AdminInfoResult()
}

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
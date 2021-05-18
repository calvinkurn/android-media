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
        @SerializedName("location_list")
        @Expose
        val locationList: List<ShopLocationResponse>? = listOf(),
        @SerializedName("response_detail")
        @Expose
        val responseDetail: AdminResponseDetail? = AdminResponseDetail()
)

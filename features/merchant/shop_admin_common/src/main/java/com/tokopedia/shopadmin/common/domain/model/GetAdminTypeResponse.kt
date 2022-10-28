package com.tokopedia.shopadmin.common.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetAdminTypeResponse(
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
            @SerializedName("detail_information")
            @Expose
            val detailInformation: DetailInformation = DetailInformation(),
            @SerializedName("status")
            @Expose
            val status: String = ""
        ) {
            data class DetailInformation(
                @SerializedName("admin_role_type")
                @Expose
                val adminRoleType: AdminRoleType = AdminRoleType()
            ) {
                data class AdminRoleType(
                    @SerializedName("is_shop_admin")
                    @Expose
                    val isShopAdmin: Boolean = false,
                    @SerializedName("is_shop_owner")
                    @Expose
                    val isShopOwner: Boolean = false
                )
            }
        }
    }
}
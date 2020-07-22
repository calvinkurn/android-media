package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomGetUserRoleResponse(
        @SerializedName("GoldGetUserShopInfo")
        @Expose
        val goldGetUserShopInfo: GoldGetUserShopInfo?
)

data class GoldGetUserShopInfo(
        @SerializedName("Data")
        @Expose
        val data: SomGetUserRoleDataModel?
)

data class SomGetUserRoleDataModel(
        @SerializedName("Roles")
        @Expose
        var roles: List<String> = emptyList()
)
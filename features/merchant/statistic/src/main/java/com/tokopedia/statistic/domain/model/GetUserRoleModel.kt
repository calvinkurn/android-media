package com.tokopedia.statistic.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetUserRoleModel(
        @Expose
        @SerializedName("GoldGetUserShopInfo")
        val goldGetUserShopInfo: GoldGetUserShopInfoModel?
)

data class GoldGetUserShopInfoModel(
        @Expose
        @SerializedName("Data")
        val `data`: RoleModel?
)

data class RoleModel(
        @Expose
        @SerializedName("Roles")
        val roles: List<String> = emptyList()
)
package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomGetUserRoleResponse(
        @SerializedName("Data")
        @Expose
        val data: SomGetUserRoleDataModel?
)

data class SomGetUserRoleDataModel(
        @SerializedName("Roles")
        @Expose
        val roles: List<Roles> = emptyList()
)

enum class Roles {
    MANAGE_SHOP,
    MANAGE_TX,
    MANAGE_INBOX,
    MANAGE_SHOPSTATS,
    MANAGE_TA,
    MANAGE_KS
}
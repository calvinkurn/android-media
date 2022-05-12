package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import com.google.gson.annotations.SerializedName

data class ParamShopInfoByID(
    @SerializedName("shopIDs")
    val shopIDs: List<Long>,
    @SerializedName("fields")
    val fields: List<String> = listOf("core", "assets")
)
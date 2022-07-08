package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ParamShopInfoByID(
    @SuppressLint("Invalid Data Type")
    @SerializedName("shopIDs")
    val shopIDs: List<Long>,
    @SerializedName("fields")
    val fields: List<String> = listOf("core", "assets")
)
package com.tokopedia.navigation_common.usecase.pojo.walletapp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GlobalMenuText(
    @Expose
    @SerializedName("id")
    val id: String = ""
)
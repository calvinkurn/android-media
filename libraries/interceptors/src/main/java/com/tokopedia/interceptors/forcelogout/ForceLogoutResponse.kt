package com.tokopedia.interceptors.forcelogout

import com.google.gson.annotations.SerializedName
import com.tokopedia.interceptors.refreshtoken.RefreshTokenData

data class ForceLogoutResponse(
    @SerializedName("force_logout_info")
    var data: ForceLogoutData? = ForceLogoutData()
)

data class ForceLogoutData (
    @SerializedName("is_force_logout")
    val isForceLogout: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("url")
    val url: String = ""
)
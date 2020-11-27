package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminInfoResponse(
    @Expose
    @SerializedName("getAdminInfo")
    val response: AdminDataResponse
)
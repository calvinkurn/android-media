package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminTypeResponse(
    @Expose
    @SerializedName("getAdminType")
    val response: AdminDataResponse = AdminDataResponse()
)
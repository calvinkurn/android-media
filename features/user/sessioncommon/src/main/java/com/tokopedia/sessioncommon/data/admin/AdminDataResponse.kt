package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminDataResponse(
    @Expose
    @SerializedName("admin_data")
    val data: AdminData
)
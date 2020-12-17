package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminData(
    @Expose
    @SerializedName("admin_type_text")
    val adminTypeText: String?,
    @Expose
    @SerializedName("detail_information")
    val detail: AdminDetailInformation
)
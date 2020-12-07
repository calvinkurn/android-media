package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminData(
    @Expose
    @SerializedName("location_list")
    val locations: List<AdminShopLocation>,
    @Expose
    @SerializedName("detail_information")
    val detail: AdminDetailInformation
)
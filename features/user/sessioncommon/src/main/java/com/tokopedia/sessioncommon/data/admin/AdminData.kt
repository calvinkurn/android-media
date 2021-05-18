package com.tokopedia.sessioncommon.data.admin

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AdminData(
    @Expose
    @SerializedName("admin_type_string")
    val adminTypeText: String? = "",
    @Expose
    @SerializedName("detail_information")
    val detail: AdminDetailInformation = AdminDetailInformation(),
    @Expose
    @SerializedName("status")
    val status: String? = ""
) {

    companion object {
        private const val ACTIVE_STATUS = "1"
    }

    fun isShopActive(): Boolean = status == ACTIVE_STATUS

}
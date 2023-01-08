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
        const val ADMIN_WAITING_CONFIRMATION = "8"
        const val ADMIN_REJECT = "9"
        const val ADMIN_EXPIRED = "10"
    }

    fun isShopActive(): Boolean = status == ACTIVE_STATUS

    fun isAdminInvitation(): Boolean = status in listOf(
        ADMIN_WAITING_CONFIRMATION,
        ADMIN_EXPIRED,
        ADMIN_REJECT
    )

}

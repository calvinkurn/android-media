package com.tokopedia.settingbank.banklist.domain.pojo

/**
 * @author by nisie on 6/20/18.
 */

data class DeleteBankAccountPojo(
        @Expose
        @SerializedName("is_success")
        val is_success: Boolean? = false
)
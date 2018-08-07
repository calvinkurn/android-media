package com.tokopedia.settingbank.banklist.domain.pojo

/**
 * @author by nisie on 6/8/18.
 */
data class SetDefaultBankAccountPojo(
        @Expose
        @SerializedName("is_success")
        val is_success: Boolean? = false
)


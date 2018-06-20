package com.tokopedia.settingbank.domain.pojo

/**
 * @author by nisie on 6/8/18.
 */
data class SetDefaultBankAccountPojo(
        val data: SetDefaultBankAccountData? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class SetDefaultBankAccountData(
        val is_success: Int? = 0
)


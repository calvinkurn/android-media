package com.tokopedia.settingbank.addeditaccount.domain.pojo

/**
 * @author by nisie on 6/22/18.
 */
data class AddBankAccountPojo(
        val data: AddBankAccountData? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class AddBankAccountData(
        val is_success: Int? = 0
)
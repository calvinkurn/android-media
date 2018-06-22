package com.tokopedia.settingbank.banklist.domain.pojo

/**
 * @author by nisie on 6/20/18.
 */

data class DeleteBankAccountPojo(
        val data: DeleteBankAccountData? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class DeleteBankAccountData(
        val is_success: Int? = 0
)
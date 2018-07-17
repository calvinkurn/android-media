package com.tokopedia.settingbank.choosebank.domain.pojo

/**
 * @author by nisie on 7/2/18.
 */
data class BankListPojo(
        val data: GetListBankData? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class GetListBankData(
        val has_next_page: Boolean? = false,
        val banks: List<BankAccount> = ArrayList()
)

data class BankAccount(
        val bank_id: String? = "",
        val bank_name: String? = "",
        val clearing_code: String? = "",
        val abbreviation: String? = ""
)

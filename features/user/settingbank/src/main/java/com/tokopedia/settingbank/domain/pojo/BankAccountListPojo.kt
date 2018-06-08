package com.tokopedia.settingbank.domain.pojo

/**
 * @author by nisie on 6/8/18.
 */
data class BankAccountListPojo(
        val data: Data? = null,
        val message_error: List<String>? = ArrayList(),
        val message_status: List<String>? = ArrayList()
)

data class Data(
        val paging: PagingModel? = PagingModel(),
        val list: List<BankAccount> = ArrayList()
)

data class PagingModel(
        val uri_next: String? = "",
        val uri_previous: String? = "",
        val current: String? = ""
)

data class BankAccount(
        val bank_id: Int = 0,
        val bank_branch: String? = "",
        val bank_account_name: String? = "",
        val bank_account_number: String? = "",
        val is_verified_account: Int = 0,
        val bank_account_id: String? = "",
        val bank_name: String? = "",
        val is_default_bank: Int = 0,
        val bank_logo: String? = ""
)



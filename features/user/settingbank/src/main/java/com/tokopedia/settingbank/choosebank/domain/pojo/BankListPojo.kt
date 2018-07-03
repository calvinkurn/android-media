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
        val paging: PagingModel? = PagingModel(),
        val list: List<BankAccount> = ArrayList()
)

data class PagingModel(
        val uri_next: String? = "",
        val uri_previous: String? = "",
        val current: String? = ""
)

data class BankAccount(
        val bank_id: String? = "",
        val bank_name: String? = "",
        val bank_clearing_code: String? = "",
        val bank_abbreviation: String? = ""
)

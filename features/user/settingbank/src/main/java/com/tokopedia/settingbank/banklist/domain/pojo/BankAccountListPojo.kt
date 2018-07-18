package com.tokopedia.settingbank.banklist.domain.pojo

/**
 * @author by nisie on 6/8/18.
 */
data class BankAccountListPojo(
        val paging: PagingModel? = PagingModel(),
        val bank_accounts: List<BankAccount> = ArrayList(),
        val user_info: UserInfo = UserInfo()
)

data class PagingModel(
        val uri_next: String? = "",
        val uri_previous: String? = "",
        val current: String? = ""
)

data class BankAccount(
        val bank_id: String? = "",
        val acc_id: String? = "",
        val acc_name: String? = "",
        val branch: String? = "",
        val bank_name: String? = "",
        val acc_number: String? = "",
        val primary: Boolean? = false,
        val bank_image_url: String? = "",
        val type: Int? = 0
)

data class UserInfo(
        val is_verified: Boolean? = false,
        val messages: String? = ""
)
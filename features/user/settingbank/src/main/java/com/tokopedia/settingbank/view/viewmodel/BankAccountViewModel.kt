package com.tokopedia.settingbank.view.viewmodel

/**
 * @author by nisie on 6/8/18.
 */

data class BankAccountViewModel(
        val bankId: String? = "",
        val branchName: String? = "",
        val accountName: String? = "",
        val accountNumber: String? = "",
        val isVerifiedAccount: Boolean = false,
        val accountId: String? = "",
        val bankName: String? = "",
        val isDefaultBank: Boolean = false,
        val bankLogo: String? = ""
)
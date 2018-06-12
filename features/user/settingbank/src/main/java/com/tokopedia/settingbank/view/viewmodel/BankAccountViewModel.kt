package com.tokopedia.settingbank.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingbank.view.adapter.BankAccountTypeFactory

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
        val bankLogo: String? = "")
    : Visitable<BankAccountTypeFactory> {
    override fun type(typeFactory: BankAccountTypeFactory): Int {
        return typeFactory.type(this)
    }


}
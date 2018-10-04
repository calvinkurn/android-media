package com.tokopedia.settingbank.banklist.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingbank.banklist.view.adapter.BankAccountTypeFactory

/**
 * @author by nisie on 6/8/18.
 */

data class BankAccountViewModel(
        var bankId: String? = "",
        var branchName: String? = "",
        var accountName: String? = "",
        var accountNumber: String? = "",
        var accountId: String? = "",
        var bankName: String? = "",
        var isDefaultBank: Boolean? = false,
        var bankLogo: String? = "")
    : Visitable<BankAccountTypeFactory> {
    override fun type(typeFactory: BankAccountTypeFactory): Int {
        return typeFactory.type(this)
    }

}
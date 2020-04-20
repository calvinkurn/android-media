package com.tokopedia.withdraw.view

import com.tokopedia.withdraw.domain.model.BankAccount

interface WithdrawalFragmentCallback {
    fun openSuccessFragment(bankAccount: BankAccount, message: String, amount: Long)
}
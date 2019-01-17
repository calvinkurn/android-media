package com.tokopedia.instantloan.ddcollector.account

import android.accounts.AccountManager
import com.tokopedia.instantloan.ddcollector.BaseCollector

class Account(private val mAccountManager: AccountManager) : BaseCollector() {

    override fun getType(): String {
        return DD_ACCOUNT
    }

    override fun getData(): List<Map<String, String>> = mAccountManager.accounts.map { mapOf(TYPE to it.type, NAME to it.name) }

    companion object {

        val DD_ACCOUNT = "account"
        val NAME = "account"
        val TYPE = "type"
    }
}

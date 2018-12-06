//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tokopedia.instantloan.ddcollector.account

import android.accounts.AccountManager
import android.annotation.SuppressLint

import com.tokopedia.instantloan.ddcollector.BaseCollector

import java.util.ArrayList
import java.util.HashMap

class Account(private val mAccountManager: AccountManager) : BaseCollector() {

    override fun getType(): String {
        return DD_ACCOUNT
    }

    @SuppressLint("MissingPermission")
    override fun getData(): Any {
        val accounts = ArrayList<Map<String, String>>()
        var accountMap: MutableMap<String, String>
        for (account in mAccountManager.accounts) {
            accountMap = HashMap()
            accountMap[TYPE] = account.type
            accountMap[NAME] = account.name
            accounts.add(accountMap)
        }

        return accounts
    }

    companion object {

        val DD_ACCOUNT = "account"
        val NAME = "account"
        val TYPE = "type"
    }
}

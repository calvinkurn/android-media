package com.tokopedia.settingbank.banklist.view.viewmodel

/**
 * @author by nisie on 6/8/18.
 */

data class BankAccountListViewModel(
        val list: ArrayList<BankAccountViewModel>? = ArrayList(),
        val enableAddButton: Boolean = false,
        val reason: String = ""
)
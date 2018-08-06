package com.tokopedia.settingbank.choosebank.view.viewmodel

/**
 * @author by nisie on 7/3/18.
 */
data class BankListViewModel(
        val list: ArrayList<BankViewModel>? = ArrayList(),
        val hasNextPage: Boolean
)
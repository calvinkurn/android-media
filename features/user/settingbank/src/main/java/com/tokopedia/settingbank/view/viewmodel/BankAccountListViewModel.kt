package com.tokopedia.settingbank.view.viewmodel

import com.tokopedia.settingbank.domain.pojo.BankAccount
import com.tokopedia.settingbank.domain.pojo.PagingModel

/**
 * @author by nisie on 6/8/18.
 */

data class BankAccountListViewModel(
        val list: List<BankAccountViewModel>? = ArrayList()
)
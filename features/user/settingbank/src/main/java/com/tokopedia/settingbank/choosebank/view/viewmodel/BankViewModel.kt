package com.tokopedia.settingbank.choosebank.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactory

/**
 * @author by nisie on 6/22/18.
 */

data class BankViewModel(
        var bankId: String? = "",
        var bankName: String? = "",
        var isSelected: Boolean = false)
    : Visitable<BankTypeFactory> {
    override fun type(typeFactory: BankTypeFactory): Int {
        return typeFactory.type(this)
    }


}
package com.tokopedia.settingbank.choosebank.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactory

/**
 * @author by nisie on 7/23/18.
 */

class EmptySearchBankViewModel : Visitable<BankTypeFactory> {

    override fun type(typeFactory: BankTypeFactory): Int {
        return typeFactory.type(this)
    }

}
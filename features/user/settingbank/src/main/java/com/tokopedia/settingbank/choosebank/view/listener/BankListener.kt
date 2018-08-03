package com.tokopedia.settingbank.choosebank.view.listener

import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel

/**
 * @author by nisie on 6/22/18.
 */
interface BankListener {

    fun onBankSelected(adapterPosition: Int, element: BankViewModel?)

}
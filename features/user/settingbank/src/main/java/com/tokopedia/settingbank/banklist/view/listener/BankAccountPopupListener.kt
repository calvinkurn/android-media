package com.tokopedia.settingbank.banklist.view.listener

import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel

/**
 * @author by nisie on 6/20/18.
 */
interface BankAccountPopupListener {

    fun makeMainAccount(adapterPosition: Int, element: BankAccountViewModel?)

    fun editBankAccount(adapterPosition: Int, element: BankAccountViewModel?)

    fun deleteBankAccount(adapterPosition: Int, element: BankAccountViewModel?)

}
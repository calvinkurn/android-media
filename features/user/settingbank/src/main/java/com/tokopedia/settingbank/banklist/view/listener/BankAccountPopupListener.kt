package com.tokopedia.settingbank.banklist.view.listener

/**
 * @author by nisie on 6/20/18.
 */
interface BankAccountPopupListener {

    fun makeMainAccount(adapterPosition: Int)

    fun editBankAccount(adapterPosition: Int)

    fun deleteBankAccount(adapterPosition: Int)

}
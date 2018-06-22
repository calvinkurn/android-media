package com.tokopedia.settingbank.addeditaccount.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel

/**
 * @author by nisie on 6/21/18.
 */

interface AddEditBankContract {

    interface View : CustomerView {
        fun getContext(): Context

    }

    interface Presenter : CustomerPresenter<View> {

        fun addBank(bankFormModel: BankFormModel)

        fun editBank(bankFormModel: BankFormModel)

        fun isValidForm(accountName: String, accountNumber: String, bankName: String): Boolean


    }
}
package com.tokopedia.settingbank.addeditaccount.view.listener

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel

/**
 * @author by nisie on 6/21/18.
 */

interface AddEditBankContract {

    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun resetError()

        fun hideLoading()

        fun onSuccessAddEditBank(accountId: String)

        fun onErrorAccountNumber(errorMessage: String)

        fun onErrorAccountName(errorMessage: String)

        fun onErrorGeneral(errorMessage: String?)

        fun onCloseForm()

        fun onErrorAddBank(errorMessage: String)

        fun onErrorEditBank(errorMessage: String)

        fun onSuccessValidateForm(bankFormModel: BankFormModel)

    }

    interface Presenter : CustomerPresenter<View> {

        fun addBank(bankFormModel: BankFormModel)

        fun editBank(bankFormModel: BankFormModel)

        fun isValidForm(accountName: String, accountNumber: String, bankName: String): Boolean

        fun validateBank(bankFormModel: BankFormModel)

        fun getCotpIntent(context: Context?): Intent


    }
}
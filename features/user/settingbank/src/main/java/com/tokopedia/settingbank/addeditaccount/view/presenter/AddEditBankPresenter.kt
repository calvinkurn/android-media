package com.tokopedia.settingbank.addeditaccount.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.user.session.UserSession

/**
 * @author by nisie on 6/22/18.
 */
class AddEditBankPresenter(private val userSession: UserSession) :
        AddEditBankContract.Presenter,
        BaseDaggerPresenter<AddEditBankContract.View>() {

    override fun addBank(bankFormModel: BankFormModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editBank(bankFormModel: BankFormModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValidForm(accountName: String, accountNumber: String, bankName: String): Boolean {
        var isValid = true

        if (accountName.isBlank()) {
            isValid = false
        }

        if (accountNumber.isBlank()) {
            isValid = false
        }

        if (bankName.isBlank()) {
            isValid = false
        }

        return isValid
    }

}
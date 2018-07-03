package com.tokopedia.settingbank.addeditaccount.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.settingbank.addeditaccount.domain.usecase.AddBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.EditBankUseCase
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 6/22/18.
 */
class AddEditBankPresenter(private val userSession: UserSession,
                           private val addBankUseCase: AddBankUseCase,
                           private val editBankUseCase: EditBankUseCase) :
        AddEditBankContract.Presenter,
        BaseDaggerPresenter<AddEditBankContract.View>() {

    val accountNumber = "nomor rekening"
    val accountName = "nama rekening"

    override fun addBank(bankFormModel: BankFormModel) {
        view.showLoading()
        view.resetError()
        addBankUseCase.execute(AddBankUseCase.getParam(
                userSession.userId,
                userSession.deviceId,
                bankFormModel.accountName,
                bankFormModel.accountNumber,
                bankFormModel.bankName
        ), object : Subscriber<String>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)

                when {
                    errorMessage.toLowerCase().contains(accountNumber) -> view.onErrorAccountNumber(errorMessage)
                    errorMessage.toLowerCase().contains(accountName) -> view.onErrorAccountName(errorMessage)
                    else -> view.onErrorGeneral(errorMessage)
                }
            }

            override fun onNext(statusMessage: String) {
                view.hideLoading()
                view.onSuccessAddEditBank(statusMessage)
            }
        })
    }

    override fun editBank(bankFormModel: BankFormModel) {
        view.showLoading()
        view.resetError()
        editBankUseCase.execute(AddBankUseCase.getParam(
                userSession.userId,
                userSession.deviceId,
                bankFormModel.accountName,
                bankFormModel.accountNumber,
                bankFormModel.bankName
        ), object : Subscriber<String>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)

                when {
                    errorMessage.toLowerCase().contains(accountNumber) -> view.onErrorAccountNumber(errorMessage)
                    errorMessage.toLowerCase().contains(accountName) -> view.onErrorAccountName(errorMessage)
                    else -> view.onErrorGeneral(ErrorHandler.getErrorMessage(view.getContext(), e))
                }
            }

            override fun onNext(statusMessage: String) {
                view.hideLoading()
                view.onSuccessAddEditBank(statusMessage)
            }
        })
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

    override fun detachView() {
        super.detachView()
        addBankUseCase.unsubscribe()
        editBankUseCase.unsubscribe()
    }

}
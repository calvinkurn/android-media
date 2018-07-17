package com.tokopedia.settingbank.addeditaccount.view.presenter

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.settingbank.addeditaccount.domain.usecase.AddBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.EditBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.ValidateBankUseCase
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import com.tokopedia.user.session.UserSession
import rx.Subscriber

/**
 * @author by nisie on 6/22/18.
 */
class AddEditBankPresenter(private val userSession: UserSession,
                           private val addBankUseCase: AddBankUseCase,
                           private val editBankUseCase: EditBankUseCase,
                           private val validateBankUseCase: ValidateBankUseCase) :
        AddEditBankContract.Presenter,
        BaseDaggerPresenter<AddEditBankContract.View>() {

    override fun validateBank(bankFormModel: BankFormModel) {
        view.showLoading()
        view.resetError()

        validateBankUseCase.execute(ValidateBankUseCase.getParamEdit(
                bankFormModel.accountId,
                bankFormModel.accountName,
                bankFormModel.accountNumber,
                bankFormModel.bankId
        ), object : Subscriber<ValidateBankViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                showError("", errorMessage)
            }

            override fun onNext(validateBankViewModel: ValidateBankViewModel) {
                view.hideLoading()

                if (validateBankViewModel.isSuccess != null
                        && validateBankViewModel.isSuccess) {

                    if (bankFormModel.status == BankFormModel.Companion.STATUS_ADD
                            || (validateBankViewModel.isDataChanged != null
                                    && validateBankViewModel.isDataChanged
                                    && bankFormModel.status == BankFormModel.Companion
                                    .STATUS_EDIT)) {
                        view.onGoToCOTP()
                    } else {
                        view.onCloseForm()
                    }

                } else if (!validateBankViewModel.paramName.isNullOrBlank()
                        && !validateBankViewModel.message.isNullOrBlank()) {
                    val messageError = validateBankViewModel.message
                    showError(validateBankViewModel.paramName!!, messageError!!)
                }
            }
        })
    }

    private fun showError(paramName: String, messageError: String) {
        val paramAccountNumber = "acc_no"
        val paramAccountName = "acc_name"

        val accountNumber = "nomor rekening"
        val accountName = "nama rekening"

        when {
            paramName.equals(paramAccountNumber) -> view.onErrorAccountNumber(messageError)
            paramName.equals(paramAccountName) -> view.onErrorAccountName(messageError)
            messageError.toLowerCase().contains(accountNumber) -> view.onErrorAccountNumber(messageError)
            messageError.toLowerCase().contains(accountName) -> view.onErrorAccountName(messageError)
            else -> view.onErrorGeneral(messageError)
        }
    }

    override fun addBank(bankFormModel: BankFormModel) {
        view.showLoading()
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
                view.onErrorAddBank(errorMessage)
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
                view.onErrorEditBank(errorMessage)
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

    override fun getCotpIntent(context: Context?): Intent {
        return VerificationActivity.getShowChooseVerificationMethodIntent(context, RequestOtpUseCase
                .OTP_TYPE_ADD_BANK_ACCOUNT, userSession.phoneNumber, userSession.email)
    }

    override fun detachView() {
        super.detachView()
        validateBankUseCase.unsubscribe()
        addBankUseCase.unsubscribe()
        editBankUseCase.unsubscribe()
    }

}
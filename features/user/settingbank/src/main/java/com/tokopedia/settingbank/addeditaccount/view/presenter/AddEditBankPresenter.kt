package com.tokopedia.settingbank.addeditaccount.view.presenter

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.settingbank.addeditaccount.domain.pojo.AddBankAccountPojo
import com.tokopedia.settingbank.addeditaccount.domain.usecase.AddBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.EditBankUseCase
import com.tokopedia.settingbank.addeditaccount.domain.usecase.ValidateBankUseCase
import com.tokopedia.settingbank.addeditaccount.view.listener.AddEditBankContract
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidateBankViewModel
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.ValidationForm
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 6/22/18.
 */
class AddEditBankPresenter @Inject constructor (private val userSession: UserSessionInterface,
                           private val addBankUseCase: AddBankUseCase,
                           private val editBankUseCase: EditBankUseCase,
                           private val validateBankUseCase: ValidateBankUseCase) :
        AddEditBankContract.Presenter,
        BaseDaggerPresenter<AddEditBankContract.View>() {

    override fun validateBank(bankFormModel: BankFormModel) {
        view.showLoading()
        view.resetError()

        validateBankUseCase.execute(ValidateBankUseCase.getParam(
                bankFormModel.accountId,
                bankFormModel.accountName,
                bankFormModel.accountNumber,
                bankFormModel.bankId,
                bankFormModel.bankName
        ), object : Subscriber<ValidateBankViewModel>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                showErrorGeneral(e)
            }

            override fun onNext(validateBankViewModel: ValidateBankViewModel) {
                view.hideLoading()

                if (validateBankViewModel.isSuccess != null && validateBankViewModel.isSuccess) {

                    if (isFormActuallyChanged(bankFormModel, validateBankViewModel)) {
                        view.onSuccessValidateForm(bankFormModel)
                    } else {
                        view.onCloseForm()
                    }

                } else if (validateBankViewModel.listValidation.isNotEmpty()) {
                    showErrorForm(validateBankViewModel.listValidation)
                }
            }
        })
    }

    private fun isFormActuallyChanged(bankFormModel: BankFormModel, validateBankViewModel: ValidateBankViewModel): Boolean {
        return bankFormModel.status == BankFormModel.Companion.STATUS_ADD
                || (validateBankViewModel.isDataChanged != null
                && validateBankViewModel.isDataChanged
                && bankFormModel.status == BankFormModel.Companion.STATUS_EDIT)
    }

    private fun showErrorForm(listValidation: List<ValidationForm>) {
        val paramAccountNumber = "acc_no"
        val paramAccountName = "acc_name"

        for (form in listValidation) {
            when {
                form.paramName.equals(paramAccountNumber) -> view.onErrorAccountNumber(form.message
                        ?: "")
                form.paramName.equals(paramAccountName) -> view.onErrorAccountName(form.message
                        ?: "")
            }
        }
    }

    private fun showErrorGeneral(e: Throwable) {
        val messageError: String = ErrorHandler.getErrorMessage(view.getContext(), e)

        val accountNumber = "nomor rekening"
        val accountName = "nama rekening"

        when {
            messageError.toLowerCase().contains(accountNumber) -> view.onErrorAccountNumber(messageError)
            messageError.toLowerCase().contains(accountName) -> view.onErrorAccountName(messageError)
            else -> view.onErrorGeneral(messageError)
        }

    }

    override fun addBank(bankFormModel: BankFormModel) {
        view.showLoading()
        addBankUseCase.execute(object : Subscriber<AddBankAccountPojo>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                view.onErrorAddBank(errorMessage)
            }

            override fun onNext(pojo: AddBankAccountPojo) {
                view.hideLoading()
                if (pojo.is_success) {
                    view.onSuccessAddEditBank(pojo.acc_id)
                } else {
                    view.onErrorAddBank("")
                }
            }
        })
    }

    override fun editBank(bankFormModel: BankFormModel) {
        view.showLoading()
        view.resetError()
        editBankUseCase.execute(object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                view.hideLoading()
                val errorMessage: String = ErrorHandler.getErrorMessage(view.getContext(), e)
                view.onErrorEditBank(errorMessage)
            }

            override fun onNext(isSuccess: Boolean) {
                view.hideLoading()
                if (isSuccess) {
                    view.onSuccessAddEditBank("")
                } else {
                    view.onErrorEditBank("")
                }
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
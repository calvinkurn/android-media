package com.tokopedia.flight.resend_email.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.flight.resend_email.domain.FlightOrderResendEmailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

/**
 * @author by furqan on 11/11/2020
 */
class FlightResendETicketViewModel @Inject constructor(
        private val eticketUseCase: FlightOrderResendEmailUseCase,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    var userEmail: String = ""
    var invoiceId: String = ""

    private val mutableEmailValidation = MutableLiveData<Result<Boolean>>()
    val emailValidation: LiveData<Result<Boolean>>
        get() = mutableEmailValidation

    private val mutableResendETicketStatus = MutableLiveData<Result<Boolean>>()
    val resendETicketStatus: LiveData<Result<Boolean>>
        get() = mutableResendETicketStatus

    fun sendEticket(email: String?) {
        userEmail = email ?: userEmail
        if (isValidEmailInput(userEmail)) {
            launchCatchError(dispatcherProvider.main, block = {
                val status = eticketUseCase.executeResendETicket(invoiceId, userEmail)
                mutableResendETicketStatus.postValue(Success(status))
            }) {
                it.printStackTrace()
                mutableResendETicketStatus.postValue(Fail(it))
            }
        }
    }

    fun isValidEmailInput(userEmail: String?): Boolean {
        var isValid = true

        if (userEmail.isNullOrEmpty()) {
            isValid = false
            mutableEmailValidation.postValue(Fail(Throwable(EMPTY_EMAIL_ERROR_ID.toString())))
        } else if (!isValidEmail(userEmail)) {
            isValid = false
            mutableEmailValidation.postValue(Fail(Throwable(INVALID_EMAIL_ERROR_ID.toString())))
        } else if (!isEmailWithoutProhibitSymbol(userEmail)) {
            isValid = false
            mutableEmailValidation.postValue(Fail(Throwable(INVALID_SYMBOL_EMAIL_ERROR_ID.toString())))
        }

        if (isValid) {
            mutableEmailValidation.postValue(Success(true))
        }

        return isValid
    }

    private fun isValidEmail(userEmail: String): Boolean =
            Patterns.EMAIL_ADDRESS.matcher(userEmail).matches() &&
                    !userEmail.contains(".@") &&
                    !userEmail.contains("@.")

    private fun isEmailWithoutProhibitSymbol(userEmail: String): Boolean =
            !userEmail.contains("+")

    companion object {
        const val EMPTY_EMAIL_ERROR_ID = 1
        const val INVALID_EMAIL_ERROR_ID = 2
        const val INVALID_SYMBOL_EMAIL_ERROR_ID = 3
    }

}
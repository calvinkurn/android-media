package com.tokopedia.flight.orderdetail.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.orderdetail.domain.FlightOrderDetailGetInvoiceEticketUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 11/11/2020
 */
class FlightResendETicketViewModel @Inject constructor(
        private val eticketUseCase: FlightOrderDetailGetInvoiceEticketUseCase,
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    var userId: String = ""
    var userEmail: String = ""
    var invoiceId: String = ""

    private val mutableResendETicketStatus = MutableLiveData<Result<Boolean>>()
    val resendETicketStatus: LiveData<Result<Boolean>>
        get() = mutableResendETicketStatus

    fun sendEticket(email: String?) {
        if (isValidEmailInput(userEmail)) {
            launchCatchError(dispatcherProvider.ui(), block = {
                val status = eticketUseCase.executeResendETicket(invoiceId, email ?: "")
                mutableResendETicketStatus.postValue(Success(status))
            }) {
                it.printStackTrace()
                mutableResendETicketStatus.postValue(Fail(it))
            }
        }
    }

    private fun isValidEmailInput(userEmail: String?): Boolean {
        var isValid = true

        if (userEmail.isNullOrEmpty()) {
            isValid = false
            mutableResendETicketStatus.postValue(Fail(Throwable(EMPTY_EMAIL_ERROR_ID.toString())))
        } else if (!isValidEmail(userEmail)) {
            isValid = false
            mutableResendETicketStatus.postValue(Fail(Throwable(INVALID_EMAIL_ERROR_ID.toString())))
        } else if (!isEmailWithoutProhibitSymbol(userEmail)) {
            isValid = false
            mutableResendETicketStatus.postValue(Fail(Throwable(INVALID_SYMBOL_EMAIL_ERROR_ID.toString())))
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
package com.tokopedia.flight.resend_eticket.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.resend_eticket.di.DaggerFlightResendEmailComponent
import com.tokopedia.flight.resend_eticket.presentation.viewmodel.FlightResendETicketViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 12/11/2020
 */
class FlightOrderResendEmailBottomSheet : BottomSheetUnify() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightResendETicketViewModel: FlightResendETicketViewModel

    private lateinit var mChildView: View
    private lateinit var tfResendEmailAddress: TextFieldUnify
    private lateinit var btnSendEmail: UnifyButton
    private lateinit var tgError: Typography

    var userEmail: String = ""
    var invoiceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            flightResendETicketViewModel =
                viewModelProvider.get(FlightResendETicketViewModel::class.java)
            if (userEmail.isNotEmpty()) flightResendETicketViewModel.userEmail = userEmail
            if (invoiceId.isNotEmpty()) flightResendETicketViewModel.invoiceId = invoiceId
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()

        flightResendETicketViewModel.resendETicketStatus.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    onResendEticketSuccess()
                }
                is Fail -> {
                    showErrorToaster(R.string.flight_resend_eticket_dialog_general_error)
                }
            }
        })

        flightResendETicketViewModel.emailValidation.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    onEmailPassedValidation()
                }
                is Fail -> {
                    it.throwable.message?.let { message ->
                        when (message) {
                            FlightResendETicketViewModel.EMPTY_EMAIL_ERROR_ID.toString() -> {
                                showInputError(R.string.flight_resend_eticket_dialog_email_empty_error)
                            }
                            FlightResendETicketViewModel.INVALID_EMAIL_ERROR_ID.toString() -> {
                                showInputError(R.string.flight_resend_eticket_dialog_email_invalid_error)
                            }
                            FlightResendETicketViewModel.INVALID_SYMBOL_EMAIL_ERROR_ID.toString() -> {
                                showInputError(R.string.flight_resend_eticket_dialog_email_invalid_error)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun initInjector() {
        activity?.let {
            val component = DaggerFlightResendEmailComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(it.application))
                .build()
            component.inject(this)
        }
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        showKnob = false
        isDragable = true
        isHideable = false
        isKeyboardOverlap = false
        setTitle(getString(R.string.resend_eticket_bottomsheet_title))

        mChildView =
            View.inflate(requireContext(), R.layout.fragment_flight_resend_eticket_dialog, null)
        setChild(mChildView)

        tfResendEmailAddress = mChildView.findViewById(R.id.tfResendEticketEmailAddress)
        btnSendEmail = mChildView.findViewById(R.id.btnResendETicketSend)
        tgError = mChildView.findViewById(R.id.tgResendETicketError)
    }

    private fun renderView() {
        tfResendEmailAddress.textFieldInput.setText(flightResendETicketViewModel.userEmail)
        flightResendETicketViewModel.isValidEmailInput(tfResendEmailAddress.textFieldInput.text.toString())
        btnSendEmail.setOnClickListener {
            flightResendETicketViewModel.sendEticket(tfResendEmailAddress.textFieldInput.text.toString())
        }

        tfResendEmailAddress.textFieldInput.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            private var timer: Timer = Timer()
            override fun afterTextChanged(text: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        flightResendETicketViewModel.isValidEmailInput(text.toString())
                    }
                }, TEXT_DELAY)
            }
        })
    }

    private fun onResendEticketSuccess() {
        targetFragment?.onActivityResult(
            targetRequestCode,
            Activity.RESULT_OK,
            null
        )
        dismiss()
    }

    private fun onEmailPassedValidation() {
        tfResendEmailAddress.setError(false)
        tgError.visibility = View.GONE
        btnSendEmail.isEnabled = true
    }

    private fun showInputError(@StringRes resId: Int) {
        tfResendEmailAddress.setError(true)
        tgError.text = getString(resId)
        tgError.visibility = View.VISIBLE
        btnSendEmail.isEnabled = false
    }

    private fun showErrorToaster(@StringRes resId: Int) {
        Toaster.build(requireView(), getString(resId), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            .show()
    }

    companion object {
        const val TAG = "TagFlightOrderResendEmailBottomSheet"

        private const val TEXT_DELAY: Long = 200L

        fun getInstance(userEmail: String, invoiceId: String): FlightOrderResendEmailBottomSheet =
            FlightOrderResendEmailBottomSheet().also {
                it.userEmail = userEmail
                it.invoiceId = invoiceId
            }
    }

}
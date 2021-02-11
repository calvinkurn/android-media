package com.tokopedia.flight.resend_email.presentation.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.flight.orderlist.R
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent
import com.tokopedia.flight.resend_email.presentation.viewmodel.FlightResendETicketViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_resend_eticket_dialog.view.*
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

    var userEmail: String = ""
    var invoiceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightResendETicketViewModel = viewModelProvider.get(FlightResendETicketViewModel::class.java)
            if (userEmail.isNotEmpty()) flightResendETicketViewModel.userEmail = userEmail
            if (invoiceId.isNotEmpty()) flightResendETicketViewModel.invoiceId = invoiceId
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()

        flightResendETicketViewModel.resendETicketStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onResendEticketSuccess()
                }
                is Fail -> {
                    showErrorToaster(R.string.flight_resend_eticket_dialog_general_error)
                }
            }
        })

        flightResendETicketViewModel.emailValidation.observe(viewLifecycleOwner, Observer {
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
            val component = DaggerFlightOrderComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
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

        mChildView = View.inflate(requireContext(), R.layout.fragment_flight_resend_eticket_dialog, null)
        setChild(mChildView)
    }

    private fun renderView() {
        mChildView.tfResendEticketEmailAddress.textFieldInput.setText(flightResendETicketViewModel.userEmail)
        flightResendETicketViewModel.isValidEmailInput(mChildView.tfResendEticketEmailAddress.textFieldInput.text.toString())
        mChildView.btnResendETicketSend.setOnClickListener {
            flightResendETicketViewModel.sendEticket(mChildView.tfResendEticketEmailAddress.textFieldInput.text.toString())
        }

        mChildView.tfResendEticketEmailAddress.textFieldInput.addTextChangedListener(object : TextWatcher {
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
        mChildView.tfResendEticketEmailAddress.setError(false)
        mChildView.tgResendETicketError.visibility = View.GONE
        mChildView.btnResendETicketSend.isEnabled = true
    }

    private fun showInputError(@StringRes resId: Int) {
        mChildView.tfResendEticketEmailAddress.setError(true)
        mChildView.tgResendETicketError.text = getString(resId)
        mChildView.tgResendETicketError.visibility = View.VISIBLE
        mChildView.btnResendETicketSend.isEnabled = false
    }

    private fun showErrorToaster(@StringRes resId: Int) {
        Toaster.build(requireView(), getString(resId), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
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
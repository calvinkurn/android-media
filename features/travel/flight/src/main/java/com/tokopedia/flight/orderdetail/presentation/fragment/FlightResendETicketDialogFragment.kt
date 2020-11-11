package com.tokopedia.flight.orderdetail.presentation.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.orderdetail.di.DaggerFlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightResendETicketViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_resend_eticket_dialog.*
import javax.inject.Inject

/**
 * @author by furqan on 11/11/2020
 */
class FlightResendETicketDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var resendETicketViewModel: FlightResendETicketViewModel

    private var isProgrammaticallyDismissed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            val component = DaggerFlightOrderDetailComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(it.application))
                    .build()
            component.inject(this)

            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            resendETicketViewModel = viewModelProvider.get(FlightResendETicketViewModel::class.java)
            arguments?.let { args ->
                resendETicketViewModel.userId = args.getString(EXTRA_USER_ID, "")
                resendETicketViewModel.userEmail = args.getString(EXTRA_USER_EMAIL, "")
                resendETicketViewModel.invoiceId = args.getString(EXTRA_INVOICE_ID, "")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(com.tokopedia.flight.orderlist.R.layout.fragment_flight_resend_eticket_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()

        resendETicketViewModel.resendETicketStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onResendEticketSuccess()
                }
                is Fail -> {
                    it.throwable.message?.let { message ->
                        when (message) {
                            FlightResendETicketViewModel.EMPTY_EMAIL_ERROR_ID.toString() -> {
                                showInputError(com.tokopedia.flight.orderlist.R.string.flight_resend_eticket_dialog_email_empty_error)
                            }
                            FlightResendETicketViewModel.INVALID_EMAIL_ERROR_ID.toString() -> {
                                showInputError(com.tokopedia.flight.orderlist.R.string.flight_resend_eticket_dialog_email_invalid_error)
                            }
                            FlightResendETicketViewModel.INVALID_SYMBOL_EMAIL_ERROR_ID.toString() -> {
                                showInputError(com.tokopedia.flight.orderlist.R.string.flight_resend_eticket_dialog_email_invalid_symbol_error)
                            }
                            else -> {
                                showErrorToaster(message)
                            }
                        }
                    }
                }
            }
        })
    }

    override fun dismiss() {
        if (!isProgrammaticallyDismissed) {
            targetFragment?.onActivityResult(
                    targetRequestCode,
                    Activity.RESULT_CANCELED,
                    null
            )
        }
        super.dismiss()
    }

    private fun renderView() {
        tfResendEticketEmailAddress.textFieldInput.setText(resendETicketViewModel.userEmail)
        tgResendETicketSend.setOnClickListener {
            resendETicketViewModel.sendEticket(tfResendEticketEmailAddress.textFieldInput.text.toString())
        }
        tgResendETicketCancel.setOnClickListener {
            isProgrammaticallyDismissed = true
            targetFragment?.onActivityResult(
                    targetRequestCode,
                    Activity.RESULT_CANCELED,
                    null
            )
            dismiss()
        }
    }

    private fun onResendEticketSuccess() {
        isProgrammaticallyDismissed = true
        targetFragment?.onActivityResult(
                targetRequestCode,
                Activity.RESULT_OK,
                null
        )
        dismiss()
    }

    private fun showInputError(@StringRes resId: Int) {
        tfResendEticketEmailAddress.setError(true)
        tfResendEticketEmailAddress.setMessage(getString(resId))
    }

    private fun showErrorToaster(message: String) {
        Toaster.make(requireView(), message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"
        private const val EXTRA_USER_ID = "EXTRA_USER_ID"
        private const val EXTRA_USER_EMAIL = "EXTRA_USER_EMAIL"

        fun getInstance(userId: String,
                        userEmail: String,
                        invoiceId: String): FlightResendETicketDialogFragment =
                FlightResendETicketDialogFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_USER_ID, userId)
                        putString(EXTRA_USER_EMAIL, userEmail)
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }

}
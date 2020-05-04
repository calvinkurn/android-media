package com.tokopedia.payment.fingerprint.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tokopedia.fingerprint.view.FingerPrintDialog
import com.tokopedia.payment.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zulfikarrahman on 3/27/18.
 */
class FingerPrintDialogPayment : FingerPrintDialog(), FingerPrintDialog.Callback {

    private var listenerPayment: ListenerPayment? = null
    private var containerOtp: View? = null

    private var userId: String = ""
    private var date: String? = null
    private var urlOtp: String = ""
    private var transactionId: String = ""
    private var counterError = 0

    override fun getTextToEncrypt(): String {
        date = generateDate()
        return userId + date
    }

    fun setListenerPayment(listenerPayment: ListenerPayment) {
        this.listenerPayment = listenerPayment
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.partial_bottom_sheet_fingerprint_view_payment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlOtp = arguments?.getString(URL_OTP) ?: ""
        userId = arguments?.getString(USER_ID) ?: ""
        transactionId = arguments?.getString(TRANSACTION_ID) ?: ""
    }

    override fun getCallback(): Callback {
        return this
    }

    private fun generateDate(): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Calendar.getInstance().time)
    }

    override fun initView(view: View) {
        containerOtp = view.findViewById(R.id.container_otp)
        view.findViewById<Button>(R.id.button_use_otp)?.setOnClickListener { listenerPayment?.onGoToOtpPage(transactionId, urlOtp) }
    }

    private fun setVisibilityContainer() {
        containerOtp?.visibility = View.VISIBLE
        updateHeight()
    }

    private fun updateCounterError(): Boolean {
        if (isResumed) {
            counterError++
            updateTitle(getString(R.string.fingerprint_label_failed_scan))
            setVisibilityContainer()
            if (counterError > MAX_ERROR) {
                dismiss()
                listenerPayment?.onGoToOtpPage(transactionId, urlOtp)
                return false
            }
        }
        return true
    }

    override fun onCloseButtonClick() {
        listenerPayment?.onGoToOtpPage(transactionId, urlOtp)
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        updateCounterError()
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        updateCounterError()
    }

    override fun onAuthenticationSucceeded(publicKey: String?, signature: String?) {
        listenerPayment?.onPaymentFingerPrint(transactionId, publicKey, date, signature, userId)
    }

    override fun onAuthenticationFailed() {
        updateCounterError()
    }

    fun onErrorNetworkPaymentFingerPrint() {
        if (updateCounterError()) {
            startListening()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    interface ListenerPayment {
        fun onGoToOtpPage(transactionId: String, urlOtp: String)
        fun onPaymentFingerPrint(transactionId: String?, publicKey: String?, date: String?, signature: String?, userId: String?)
    }

    companion object {
        private const val MAX_ERROR = 3
        private const val USER_ID = "USER_ID"
        private const val URL_OTP = "URL_OTP"
        private const val TRANSACTION_ID = "TRANSACTION_ID"
        private const val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ"

        fun createInstance(userId: String, urlOtp: String, transactionId: String?): FingerPrintDialogPayment {
            val fingerPrintDialogPayment = FingerPrintDialogPayment()
            fingerPrintDialogPayment.arguments = Bundle().apply {
                putString(USER_ID, userId)
                putString(URL_OTP, urlOtp)
                putString(TRANSACTION_ID, transactionId)
            }
            return fingerPrintDialogPayment
        }
    }
}
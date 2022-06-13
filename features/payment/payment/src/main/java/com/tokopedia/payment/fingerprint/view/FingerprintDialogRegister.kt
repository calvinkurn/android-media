package com.tokopedia.payment.fingerprint.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.tokopedia.fingerprint.view.FingerPrintDialog
import com.tokopedia.payment.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by zulfikarrahman on 4/5/18.
 */
class FingerprintDialogRegister : FingerPrintDialog(), FingerPrintDialog.Callback {

    private var userId: String = ""
    private var transactionId: String = ""
    private var counterError = 0

    private var listenerRegister: ListenerRegister? = null
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = arguments?.getString(USER_ID) ?: ""
        transactionId = arguments?.getString(TRANSACTION_ID) ?: ""
    }

    fun setListenerRegister(listenerRegister: ListenerRegister) {
        this.listenerRegister = listenerRegister
    }

    override fun getTextToEncrypt(): String {
        date = generateDate()
        return userId + date
    }

    override fun getCallback(): Callback {
        return this
    }

    private fun generateDate(): String {
        return SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(Calendar.getInstance().time)
    }

    override fun initView(view: View) {
        super.initView(view)
        updateDesc(getString(R.string.fingerprint_label_register_fingerprint))
    }

    private fun updateCounterError(): Boolean {
        if (isResumed) {
            counterError++
            updateDesc(getString(com.tokopedia.fingerprint.R.string.fingerprint_label_desc_default))
            updateTitle(getString(R.string.fingerprint_label_try_again))
            if (counterError > MAX_ERROR) {
                stopListening()
                listenerRegister?.showErrorRegisterSnackbar()
                dismiss()
                return false
            }
        }
        return true
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
        updateCounterError()
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
        updateCounterError()
    }

    override fun onAuthenticationSucceeded(publicKey: String?, signature: String?) {
        listenerRegister?.onRegisterFingerPrint(transactionId, publicKey, date, signature, userId)
    }

    override fun onAuthenticationFailed() {
        updateCounterError()
    }

    fun onErrorRegisterFingerPrint() {
        if (updateCounterError()) {
            startListening()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
        }
    }

    interface ListenerRegister {
        fun onRegisterFingerPrint(transactionId: String?, publicKey: String?, date: String?, signature: String?, userId: String?)
        fun showErrorRegisterSnackbar()
    }

    companion object {
        private const val MAX_ERROR = 3
        private const val USER_ID = "USER_ID"
        private const val TRANSACTION_ID = "TRANSACTION_ID"
        private const val DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ"

        fun createInstance(userId: String, transactionId: String?): FingerprintDialogRegister {
            val fingerprintDialogRegister = FingerprintDialogRegister()
            fingerprintDialogRegister.arguments = Bundle().apply {
                putString(USER_ID, userId)
                putString(TRANSACTION_ID, transactionId)
            }
            return fingerprintDialogRegister
        }
    }
}
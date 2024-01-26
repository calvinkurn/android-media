package com.tokopedia.webview.verification.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver: BroadcastReceiver() {

    private lateinit var listener: ReceiveSMSListener

    fun register(context: Context, listener: ReceiveSMSListener) {
//        Toast.makeText(context, "registered", Toast.LENGTH_SHORT).show()
        val a = AppSignatureHelper(context)
        val sigs = a.appSignatures
        sigs.forEach {
            Toast.makeText(context, "sig: $it", Toast.LENGTH_SHORT).show()
            println("hahaha $it")
        }
        this.listener = listener
//        val filter = IntentFilter()
//        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
//        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        context.registerReceiver(this, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    interface ReceiveSMSListener {
        fun onReceiveOTP(otpCode: String)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        Toast.makeText(context, "On Received triggered", Toast.LENGTH_SHORT).show()
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status

            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = (extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String).orEmpty()
//                    val subMessage = message.substringAfter("masuk:")
//                    val otpDigit = Regex(REGEX_NUMERIC_PATTERN).find(subMessage)?.value?.length.orZero()
//                    val otp = subMessage.substring(0, otpDigit)
                    Toast.makeText(context, "SMS received: $message", Toast.LENGTH_SHORT).show()
                    val otp = message.takeLast(6)

                    if(::listener.isInitialized && otp.toIntOrNull() != null) {
                        Toast.makeText(context, "OTP : $otp", Toast.LENGTH_SHORT).show()
                        listener.onReceiveOTP(otp)
                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                    //Do nothing
                }
            }
        }
    }

    companion object {
        /** */
        private const val REGEX_NUMERIC_PATTERN = "^[\\d]*"
    }
}

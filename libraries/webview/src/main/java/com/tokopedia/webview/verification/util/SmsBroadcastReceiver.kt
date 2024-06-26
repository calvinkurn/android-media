package com.tokopedia.webview.verification.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsBroadcastReceiver: BroadcastReceiver() {

    private var listener: ReceiveSMSListener? = null

    fun register(context: Context, listener: ReceiveSMSListener) {
        this.listener = listener
        val filter = IntentFilter()
        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        context.registerReceiver(this, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    interface ReceiveSMSListener {
        fun onReceiveOTP(otpCode: String)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status

            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = (extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String).orEmpty()
                    val otp = findSixDigitNumber(message)

                    if(otp?.toIntOrNull() != null) {
                        listener?.onReceiveOTP(otp)
                    }
                }
                CommonStatusCodes.TIMEOUT -> {
                    //Do nothing
                }
            }
        }
    }

    private fun findSixDigitNumber(input: String): String? {
        val regex = Regex("\\b\\d{6}\\b")
        val matchResult = regex.find(input)
        return matchResult?.value
    }
}

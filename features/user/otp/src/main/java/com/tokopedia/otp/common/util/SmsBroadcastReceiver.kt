package com.tokopedia.otp.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import javax.inject.Inject

/**
 * @author by nisie on 09/01/19.
 */
class SmsBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    private lateinit var listener: ReceiveSMSListener

    fun register(context: Context, listener: ReceiveSMSListener) {
        this.listener = listener
        val filter = IntentFilter()
        filter.addAction(ACTION_SMS_RECEIVED)
        context.registerReceiver(this, filter)
    }

    interface ReceiveSMSListener {
        fun onReceiveOTP(otpCode: String)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val otp = message.substringAfter("masuk:").substring(0..6)
                    if(::listener.isInitialized
                            && otp.length == 6
                            && otp.toIntOrNull() != null)
                    listener.onReceiveOTP(otp)
                }
                CommonStatusCodes.TIMEOUT -> {
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                }
            }
        }
    }

    companion object {

        private val ACTION_SMS_RECEIVED = "com.google.android.gms.auth.api.phone.SMS_RETRIEVED"
    }
}

package com.tokopedia.otp.verification.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.SYSTEM_HIGH_PRIORITY
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
        filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        filter.priority = SYSTEM_HIGH_PRIORITY
        context.registerReceiver(this, filter)
    }

    interface ReceiveSMSListener {
        fun onReceiveOTP(otpCode: String)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    val subMessage = message.substringAfter("masuk:")
                    val otpDigit = Regex(REGEX_NUMERIC_PATTERN).find(subMessage)?.value?.length as Int
                    val otp = subMessage.substring(0, otpDigit)

                    if(::listener.isInitialized && otp.toIntOrNull() != null) {
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

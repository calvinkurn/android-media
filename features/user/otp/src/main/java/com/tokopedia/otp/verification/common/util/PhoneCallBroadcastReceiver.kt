package com.tokopedia.otp.verification.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import javax.inject.Inject

/**
 * @author rival
 * @created 02/11/2020
 *
 * @team: @minion-kevin
 */

class PhoneCallBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    private lateinit var listener: OnCallStateChange

    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var isIncomingCall = false

    fun registerReceiver(context: Context?, listener: OnCallStateChange) {
        this.listener = listener
        context?.registerReceiver(this, getIntentFilter())
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val telephony = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephony.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                if (::listener.isInitialized) {
                    onStateChanged(state, phoneNumber ?: "")
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun onStateChanged(state: Int, number: String) {
        if (lastState == state) return

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncomingCall = true
                listener.onIncomingCallStart(number)
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    listener.onIncomingCallEnded(number)
                } else if (isIncomingCall) {
                    listener.onMissedCall(number)
                }
            }
        }

        lastState = state
    }

    interface OnCallStateChange {
        fun onIncomingCallStart(phoneNumber: String)
        fun onIncomingCallEnded(phoneNumber: String)
        fun onMissedCall(phoneNumber: String)
    }

    companion object {
        private fun getIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.intent.action.PHONE_STATE")
            return intentFilter
        }
    }
}
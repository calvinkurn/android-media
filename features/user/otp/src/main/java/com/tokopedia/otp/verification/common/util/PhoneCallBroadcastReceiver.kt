package com.tokopedia.otp.verification.common.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception
import javax.inject.Inject

/**
 * @author rival
 * @created 02/11/2020
 *
 * @team: @minion-kevin
 */

class PhoneCallBroadcastReceiver @Inject constructor(): BroadcastReceiver() {

    private var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()
    private var listener: OnCallStateChange? = null

    private var lastState = TelephonyManager.CALL_STATE_IDLE
    private var isIncomingCall = false

    var isRegistered = false
    private var telephony: TelephonyManager? = null

    fun registerReceiver(context: Context?, listener: OnCallStateChange) {
        this.listener = listener

        if (!isRegistered) {
            context?.let {
                it.registerReceiver(this, getIntentFilter())
                setTelephonyListener(it)
            }
            isRegistered = true
        } else {
            sendLogTracker("PhoneCallBroadcastReceiver already registered")
        }
    }

    fun unregisterReceiver(context: Context?) {
        if (isRegistered) {
            context?.unregisterReceiver(this)
            isRegistered = false
        }
    }

    private fun setTelephonyListener(context: Context) {
        try {
            telephony = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        } catch (e: Exception) {
             e.printStackTrace()
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            telephony?.listen(object : PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    onStateChanged(state, phoneNumber.orEmpty())
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        } catch (e: Exception) {
            sendLogTracker("error [PhoneCallBroadcastReceiver#onReceive(); msg=$e]")
            e.printStackTrace()
        }
    }

    private fun onStateChanged(state: Int, number: String) {
        if (lastState == state) return

        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncomingCall = true
                listener?.onIncomingCallStart(number)
            }
            TelephonyManager.CALL_STATE_IDLE -> {
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    listener?.onIncomingCallEnded(number)
                } else if (isIncomingCall) {
                    listener?.onMissedCall(number)
                }
            }
        }

        lastState = state
    }

    private fun sendLogTracker(message: String) {
        try {
            crashlytics.recordException(Throwable(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
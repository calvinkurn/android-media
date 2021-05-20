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
    private var miscallStateListener: MiscallStateListener? = null

    var isRegistered = false
    private var telephony: TelephonyManager? = null

    fun registerReceiver(context: Context?, listener: OnCallStateChange) {
        this.listener = listener

        if (!isRegistered) {
            context?.let {
                it.registerReceiver(this, getIntentFilter())
                miscallStateListener = MiscallStateListener()
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
            if(intent?.action == ACTION_PHONE_STATE) {
                miscallStateListener?.let {
                    telephony?.listen(it, PhoneStateListener.LISTEN_CALL_STATE)
                }
            }
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

    inner class MiscallStateListener: PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            super.onCallStateChanged(state, phoneNumber)
            onStateChanged(state, phoneNumber.orEmpty())
        }
    }

    interface OnCallStateChange {
        fun onIncomingCallStart(phoneNumber: String)
        fun onIncomingCallEnded(phoneNumber: String)
        fun onMissedCall(phoneNumber: String)
    }

    companion object {
        private const val ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE"
        private fun getIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ACTION_PHONE_STATE)
            return intentFilter
        }
    }
}
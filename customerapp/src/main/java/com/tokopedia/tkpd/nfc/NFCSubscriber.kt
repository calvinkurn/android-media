package com.tokopedia.tkpd

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital

class NFCSubscriber: Application.ActivityLifecycleCallbacks {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent

    companion object {

        fun newInstance(): NFCSubscriber {
            return NFCSubscriber()
        }

        @JvmStatic
        fun onNewIntent(context: Context, intent: Intent) {
            if (intent != null &&
                    (intent.action == NfcAdapter.ACTION_TAG_DISCOVERED || intent.action == NfcAdapter.ACTION_TECH_DISCOVERED)) {
                val newIntent = RouteManager.getIntent(context, ApplinkConsInternalDigital.SMARTCARD, "calling_from_nfc")
                newIntent.putExtras(intent.extras)
                newIntent.action = intent.action
                context.startActivity(newIntent)
            }
        }

    }

    override fun onActivityPaused(activity: Activity?) {
        if (nfcAdapter != null) nfcAdapter.disableForegroundDispatch(activity)
    }

    override fun onActivityResumed(activity: Activity?) {
        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, arrayOf(), null)
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        activity?.let {
            pendingIntent = PendingIntent.getActivity(activity, 0,
                    it.intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
            nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        }
    }
}
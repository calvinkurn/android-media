package com.tokopedia.emoney.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tokopedia.common_electronic_money.activity.NfcCheckBalanceActivity
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceFragment

/**
 * applink
 * tokopedia-android-internal://digital/smartcard/emoney
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD_EMONEY)
 */

class EmoneyCheckBalanceActivity : NfcCheckBalanceActivity() {

    override fun getNewFragment(): Fragment? {
        return EmoneyCheckBalanceFragment.newInstance()
    }

    /**
     * this method will be executed first time after NFC card detected
     * @param intent is needed to process data NFC card brizzi and emoney mandiri
     */
    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            processTagIntent(intent)
        }
    }

    override fun onBackPressed() {
        if (fragment != null) {
            (fragment as EmoneyCheckBalanceFragment).sendTrackingCloseButton()
        }
        super.onBackPressed()

    }

    private fun processTagIntent(intent: Intent) {
        if (intent != null && !TextUtils.isEmpty(intent.action) &&
                (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                        intent.action == NfcAdapter.ACTION_TAG_DISCOVERED)) {
            if (fragment != null) {
                (fragment as EmoneyCheckBalanceFragment).processTagIntent(intent)
            }
        }
    }
}
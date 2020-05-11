package com.tokopedia.emoney.view.activity

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.emoney.R
import com.tokopedia.emoney.view.fragment.NfcCheckBalanceFragment

/**
 * applink
 * tokopedia-android-internal://digital/smartcard/emoneybrizzi
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD)
 */

class NfcCheckBalanceActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return NfcCheckBalanceFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = getString(R.string.emoney_toolbar_title_etoll_check_balance)
    }

    /**
     * this method will be executed first time after NFC card detected
     * @param intent is needed to process data NFC card brizzi and emoney mandiri
     */
    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            processTagIntent(intent)
        }
    }

    override fun getCloseButton(): Int {
        return com.tokopedia.resources.common.R.drawable.ic_system_close_default
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    private fun processTagIntent(intent: Intent) {
        if (intent != null && !TextUtils.isEmpty(intent.action) &&
                (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                        intent.action == NfcAdapter.ACTION_TAG_DISCOVERED)) {
            if (fragment != null) {
                (fragment as NfcCheckBalanceFragment).setOnNewIntent(intent)
            }
        }
    }
}
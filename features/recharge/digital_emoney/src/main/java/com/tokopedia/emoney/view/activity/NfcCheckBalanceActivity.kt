package com.tokopedia.emoney.view.activity

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.emoney.R
import com.tokopedia.emoney.di.DaggerDigitalEmoneyComponent
import com.tokopedia.emoney.view.fragment.NfcCheckBalanceFragment
import com.tokopedia.emoney.viewmodel.NfcCheckBalanceViewModel
import javax.inject.Inject

/**
 * applink
 * tokopedia-android-internal://digital/smartcard/emoneybrizzi
 * or
 * RouteManager.route(this, ApplinkConsInternalDigital.INTERNAL_SMARTCARD)
 */

class NfcCheckBalanceActivity : BaseSimpleActivity() {

    private lateinit var nfcCheckBalanceViewModel: NfcCheckBalanceViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getNewFragment(): Fragment? {
        initInjector()
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        nfcCheckBalanceViewModel = viewModelProvider.get(NfcCheckBalanceViewModel::class.java)

        return NfcCheckBalanceFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        processTagIntent(intent)
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

    private fun initInjector() {
        val emoneyComponent = DaggerDigitalEmoneyComponent.builder()
                .baseAppComponent((this.application as BaseMainApplication).baseAppComponent)
                .build()
        emoneyComponent.inject(this)
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
            nfcCheckBalanceViewModel.setIntentFromNfc(intent)
        }
    }
}
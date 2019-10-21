package com.tokopedia.emoney.view.activity

import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceNFCFragment

class EmoneyCheckBalanceActivity : BaseSimpleActivity() {

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getNewFragment(): Fragment? {
        val uri = intent.data
        if (uri?.getQueryParameter(DIGITAL_NFC_FROM_PDP) != null) {
            val paramFromPdp = uri.getQueryParameter(DIGITAL_NFC_FROM_PDP)
        }
        return EmoneyCheckBalanceNFCFragment.newInstance()
    }

    companion object {
        private const val DIGITAL_NFC_CALLING_TYPE = "calling_page_check_saldo"
        private const val DIGITAL_NFC_FROM_PDP = "calling_from_pdp"
        private const val DIGITAL_NFC = "calling_from_nfc"
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
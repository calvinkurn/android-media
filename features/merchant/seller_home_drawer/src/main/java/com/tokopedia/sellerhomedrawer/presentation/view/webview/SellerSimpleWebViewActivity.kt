package com.tokopedia.sellerhomedrawer.presentation.view.webview

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity

class SellerSimpleWebViewActivity : BaseWebViewActivity() {

    companion object {
        @JvmStatic
        val EXTRA_URL = "EXTRA_URL"

        @JvmStatic
        fun createIntent(context: Context, extraUrl: String): Intent {
            return Intent(context, SellerSimpleWebViewActivity::class.java).putExtra(EXTRA_URL, extraUrl)
        }

    }

    override fun getContactUsIntent(): Intent? {
        return null
    }

    override fun getNewFragment(): Fragment? {
        return SellerExternalWebViewFragment.newInstance(intent.getStringExtra(EXTRA_URL))
    }
}
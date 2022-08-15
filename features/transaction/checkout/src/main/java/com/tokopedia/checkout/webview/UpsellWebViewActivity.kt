package com.tokopedia.checkout.webview

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class UpsellWebViewActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val mUrl = intent.getStringExtra(CheckoutWebViewActivity.EXTRA_URL) ?: return null
        return UpsellWebViewFragment.newInstance(mUrl)
    }
}
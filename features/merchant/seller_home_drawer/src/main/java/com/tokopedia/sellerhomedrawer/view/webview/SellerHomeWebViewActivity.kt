package com.tokopedia.sellerhomedrawer.view.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl

class SellerHomeWebViewActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        val PARAM_BUNDLE_URL = "bundle_url"
        @JvmStatic
        private val KEY_APP_LINK_QUERY_URL = "url"
        @JvmStatic
        private val MOBILE_DOMAIN = TokopediaUrl.getInstance().MOBILEWEB

        @JvmStatic
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, SellerHomeWebViewActivity::class.java).apply {
                val bundle: Bundle = Bundle().apply {
                    putString(PARAM_BUNDLE_URL, url)
                }
                putExtras(bundle)
            }
            return intent
        }

        @DeepLink(ApplinkConst.WEBVIEW, ApplinkConst.SellerApp.WEBVIEW)
        @JvmStatic
        fun createApplinkIntent(context: Context, bundle: Bundle): Intent {
            return createIntent(context, bundle.getString(KEY_APP_LINK_QUERY_URL, MOBILE_DOMAIN))
        }
    }

    var url: String? = null

    override fun getNewFragment(): Fragment? {
        if (intent != null && intent.extras != null)
            url = intent.extras?.getString(PARAM_BUNDLE_URL)
        return SellerHomeWebViewFragment.newInstance(url)
    }
}
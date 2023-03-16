package com.tokopedia.shop.product.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment

class SimpleWebViewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment = BaseSessionWebViewFragment.newInstance(intent.getStringExtra(ARG_URL) ?: "")

    companion object {
        private const val ARG_URL = "arg_url"

        @JvmStatic
        fun createIntent(context: Context, url: String): Intent =
            Intent(context, SimpleWebViewActivity::class.java).putExtra(ARG_URL, url)
    }
}

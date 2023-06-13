package com.tokopedia.checkout.webview

import android.content.Context
import android.content.Intent
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL
import com.tokopedia.webview.ext.encodeOnce

class UpsellWebViewActivity : BaseSimpleWebViewActivity() {

    override fun createFragmentInstance(): Fragment {
        return UpsellWebViewFragment.newInstance(url, needLogin, allowOverride, pullToRefresh)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    companion object {

        fun getStartIntent(
            context: Context,
            url: String,
            showToolbar: Boolean = true,
            allowOverride: Boolean = true,
            needLogin: Boolean = false,
            title: String = ""
        ): Intent {
            return Intent(context, UpsellWebViewActivity::class.java).apply {
                putExtra(KEY_URL, url.encodeOnce())
                putExtra(KEY_TITLEBAR, showToolbar)
                putExtra(KEY_ALLOW_OVERRIDE, allowOverride)
                putExtra(KEY_NEED_LOGIN, needLogin)
                putExtra(KEY_TITLE, title)
            }
        }
    }
}

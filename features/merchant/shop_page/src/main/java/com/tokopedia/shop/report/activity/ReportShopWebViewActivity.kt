package com.tokopedia.shop.report.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.shop.report.fragment.ReportStoreReportWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_ALLOW_OVERRIDE
import com.tokopedia.webview.KEY_NEED_LOGIN
import com.tokopedia.webview.KEY_TITLE
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL
import com.tokopedia.webview.ext.encodeOnce

class ReportShopWebViewActivity : BaseSimpleWebViewActivity() {

    override fun createFragmentInstance(): Fragment {
        return ReportStoreReportWebViewFragment.newInstance(url, allowOverride)
    }

    companion object {
        fun getStartIntent(
            context: Context,
            url: String
        ): Intent {
            return Intent(context, ReportShopWebViewActivity::class.java).apply {
                putExtra(KEY_URL, url.encodeOnce())
                putExtra(KEY_TITLEBAR, true)
                putExtra(KEY_ALLOW_OVERRIDE, true)
                putExtra(KEY_NEED_LOGIN, true)
                putExtra(KEY_TITLE, "")
            }
        }
    }
}

package com.tokopedia.webview.download

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL

open class BaseDownloadAppLinkActivity : BaseSimpleWebViewActivity() {
    protected var extensions: String = "";
    protected var web_url: String = ""
    companion object {
        const val KEY_EXT = "EXT"

        @JvmStatic
        @JvmOverloads
        fun newIntent(context: Context, url: String, showToolbar: Boolean,
                      extensions: String = "[pdf]"): Intent {
            return Intent(context, BaseDownloadAppLinkActivity::class.java)
                    .putExtra(KEY_URL, url)
                    .putExtra(KEY_TITLEBAR, showToolbar)
                    .putExtra(KEY_EXT, extensions)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        web_url = intent.extras?.getString(KEY_URL) ?: ""
        extensions = intent.extras?.getString(KEY_EXT) ?: ""
        super.onCreate(savedInstanceState)
    }


    override fun getNewFragment(): Fragment {
        return BaseDownloadWebViewFragment.newInstance(web_url, extensions)
    }


    object DeepLinkIntents {
        @DeepLink(ApplinkConst.WEBVIEW_DOWNLOAD)
        @JvmStatic
        fun getOrderListIntent(context: Context, extras: Bundle): Intent {

            var webUrl = extras.getString(
                    KEY_URL, TokopediaUrl.getInstance().WEB
            )
            var extensionsList = extras.getString(
                    KEY_EXT, "")

            val showToolbar: Boolean = try {
                extras.getBoolean(KEY_TITLEBAR, true)
            } catch (e: ParseException) {
                true
            }

            if (TextUtils.isEmpty(webUrl)) {
                webUrl = TokopediaUrl.getInstance().WEB
            }

            return newIntent(context, webUrl, showToolbar,extensionsList)
        }

    }

}

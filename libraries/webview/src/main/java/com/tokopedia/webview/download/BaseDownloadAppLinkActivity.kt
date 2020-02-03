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

open class BaseDownloadAppLinkActivity : BaseSimpleWebViewActivity() {
    protected var extensions: String = "";
    protected var web_url: String = ""
    companion object {
        const val KEY_APP_LINK_QUERY_URL = "url"
        const val KEY_APP_LINK_QUERY_EXTENSIONS = "ext"
        const val KEY_APP_LINK_QUERY_TITLEBAR = "titlebar"
        val ARGS_URL = "KEY_URL"
        const val KEY_SHOW_TOOLBAR = "KEY_SHOW_TOOLBAR"
        const val KEY_EXT = "EXT"

        @JvmStatic
        @JvmOverloads
        fun newIntent(context: Context, url: String, showToolbar: Boolean,
                      extensions: String = "[pdf]"): Intent {
            return Intent(context, BaseDownloadAppLinkActivity::class.java)
                    .putExtra(ARGS_URL, url)
                    .putExtra(KEY_SHOW_TOOLBAR, showToolbar)
                    .putExtra(KEY_EXT, extensions)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        web_url = intent.extras.getString(ARGS_URL)
        extensions = intent.extras.getString(KEY_EXT)
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
                    KEY_APP_LINK_QUERY_URL, TokopediaUrl.getInstance().WEB
            )
            var extensionsList = extras.getString(
                    KEY_APP_LINK_QUERY_EXTENSIONS, "")

            val showToolbar: Boolean = try {
                java.lang.Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_TITLEBAR,
                        "true"))
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

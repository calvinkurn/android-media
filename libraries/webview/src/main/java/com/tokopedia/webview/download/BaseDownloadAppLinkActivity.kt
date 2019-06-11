package com.tokopedia.webview.download

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.webview.BaseSimpleWebViewActivity

class BaseDownloadAppLinkActivity : BaseSimpleWebViewActivity() {

    companion object {
        private const val KEY_APP_LINK_QUERY_URL = "url"
        private const val KEY_APP_LINK_QUERY_EXTENSIONS = "ext"
        private const val KEY_APP_LINK_QUERY_NEED_LOGIN = "need_login"
        private const val KEY_APP_LINK_QUERY_TITLEBAR = "titlebar"
        private const val EXTRA_NEED_LOGIN = "EXTRA_NEED_LOGIN"

        private var web_url: String = ""
        private var extensions: String = ""

        val ARGS_URL = "KEY_URL"
        private const val KEY_SHOW_TOOLBAR = "KEY_SHOW_TOOLBAR"
        private const val KEY_EXT = "EXT"


        fun newIntent(context: Context, url: String, showToolbar: Boolean,
                              needLogin: Boolean, extensions: String): Intent {
            return Intent(context, BaseDownloadAppLinkActivity::class.java)
                    .putExtra(ARGS_URL, url)
                    .putExtra(KEY_SHOW_TOOLBAR, showToolbar)
                    .putExtra(EXTRA_NEED_LOGIN, needLogin)
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
                    KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
            )
            var extensionsList = extras.getString(
                    KEY_APP_LINK_QUERY_EXTENSIONS, "")

            val showToolbar: Boolean = try {
                java.lang.Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_TITLEBAR,
                        "true"))
            } catch (e: ParseException) {
                true
            }
            val needLogin: Boolean = try {
                java.lang.Boolean.parseBoolean(extras.getString(KEY_APP_LINK_QUERY_NEED_LOGIN,
                        "false"))
            } catch (e: ParseException) {
                false
            }


            if (TextUtils.isEmpty(webUrl)) {
                webUrl = TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
            }

            return newIntent(context, webUrl, showToolbar, needLogin, extensionsList)
        }

    }

}

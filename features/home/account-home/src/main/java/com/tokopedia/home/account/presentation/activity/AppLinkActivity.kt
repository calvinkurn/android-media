package com.tokopedia.home.account.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.home.account.presentation.fragment.WebViewFragment
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.webview.BaseSimpleWebViewActivity

class AppLinkActivity : BaseSimpleWebViewActivity() {

    companion object {
        private const val KEY_APP_LINK_QUERY_URL = "url"
        private const val KEY_APP_LINK_QUERY_NEED_LOGIN = "need_login"
        private const val KEY_APP_LINK_QUERY_TITLEBAR = "titlebar"
        private const val EXTRA_NEED_LOGIN = "EXTRA_NEED_LOGIN"

        private var web_url :String = ""


        private fun newIntent(context: Context, url: String, showToolbar: Boolean,
                              needLogin: Boolean): Intent {
            return Intent(context, AppLinkActivity::class.java)
                    .putExtra("KEY_URL", url)
                    .putExtra("KEY_SHOW_TOOLBAR", showToolbar)
                    .putExtra(EXTRA_NEED_LOGIN, needLogin)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        web_url =  intent.extras.getString("KEY_URL")
        super.onCreate(savedInstanceState)
    }


    override fun getNewFragment(): Fragment {
        return WebViewFragment.newInstance(web_url)
    }


    object DeepLinkIntents {
        @DeepLink("tokopedia://home/orderlist")
        @JvmStatic
        fun getOrderListIntent(context: Context, extras: Bundle): Intent {

            var webUrl = extras.getString(
                    KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
            )
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

            return newIntent(context, webUrl, showToolbar, needLogin)
        }

    }

}

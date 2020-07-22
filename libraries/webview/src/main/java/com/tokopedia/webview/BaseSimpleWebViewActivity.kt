package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.app.TaskStackBuilder
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce

open class BaseSimpleWebViewActivity : BaseSimpleActivity() {

    private lateinit var url: String
    var showTitleBar = true
    private set
    private var allowOverride = true
    private var needLogin = false
    var webViewTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        init(intent)
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        if (showTitleBar) {
            updateTitle(webViewTitle)
            supportActionBar?.show()
        } else {
            supportActionBar?.hide()
        }
    }

    private fun init(intent: Intent) {
        intent.extras?.run {
            url = getString(KEY_URL, "").decode()
            showTitleBar = getBoolean(KEY_TITLEBAR, true)
            allowOverride = getBoolean(KEY_ALLOW_OVERRIDE, true)
            needLogin = getBoolean(KEY_NEED_LOGIN, false)
            webViewTitle = getString(KEY_TITLE, DEFAULT_TITLE)
        }

        intent.data?.run {
            url = WebViewHelper.getEncodedUrlCheckSecondUrl(this, url)

            val needTitleBar = getQueryParameter(KEY_TITLEBAR)
            needTitleBar?.let {
                showTitleBar = it.toBoolean();
            }

            val override = getQueryParameter(KEY_ALLOW_OVERRIDE)
            override?.let {
                allowOverride = it.toBoolean();
            }

            val isLoginRequire = getQueryParameter(KEY_NEED_LOGIN)
            isLoginRequire?.let { needLogin = it.toBoolean() }

            val needTitle = getQueryParameter(KEY_TITLE)
            needTitle?.let { webViewTitle = it }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (showTitleBar) {
            inflater.inflate(R.menu.menu_web_view, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_home) {
            RouteManager.route(this, ApplinkConst.HOME)
        } else if (item.itemId == R.id.menu_help) {
            RouteManager.route(this, ApplinkConst.CONTACT_US)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment {
        if (::url.isInitialized) {
            return BaseSessionWebViewFragment.newInstance(url, needLogin, allowOverride)
        } else {
            this.finish()
            return Fragment()
        }
    }

    override fun onResume() {
        super.onResume()
        reloadWebViewIfNeeded()
    }

    private fun reloadWebViewIfNeeded(){
        val needReload = try {
            PersistentCacheManager.instance.get(KEY_CACHE_RELOAD_WEBVIEW, Int::class.javaPrimitiveType!!, 0) == 1
        } catch (e:Exception) {
            false
        }
        if (needReload) {
            PersistentCacheManager.instance.put(KEY_CACHE_RELOAD_WEBVIEW, 0)
            val f: Fragment? = fragment
            if (f is BaseSessionWebViewFragment) {
                f.reloadPage()
            }
        }
    }

    override fun onBackPressed() {
        val f = fragment
        if (f is BaseSessionWebViewFragment && f.webView.canGoBack()) {
            if (checkForSameUrlInPreviousIndex(f.webView)) {
                val historyItemsCount = f.webView.copyBackForwardList().size
                if (historyItemsCount > 1) {
                    goPreviousActivity()
                } else {
                    f.webView.goBack()
                    onBackPressed()
                }
            } else {
                f.webView.goBack()
            }
        } else {
            goPreviousActivity()
        }
    }

    fun goPreviousActivity() {
        if (isTaskRoot) {
            RouteManager.route(this, ApplinkConst.HOME)
            finish()
        } else {
            super.onBackPressed()
        }
    }


    fun checkForSameUrlInPreviousIndex(webView: WebView): Boolean {
        val webBackList = webView.copyBackForwardList()
        val uidKey = "uid"
        if (webBackList.size > 1) {
            val currentUrl = webBackList.getItemAtIndex(0).url
            val currentOriginalUrl = webBackList.getItemAtIndex(0).originalUrl
            val prevUrl = webBackList.getItemAtIndex(1).url
            val prevOriginalUrl = webBackList.getItemAtIndex(1).originalUrl
            if (currentUrl == prevUrl) {
                val currentUri = Uri.parse(currentOriginalUrl)
                val previousUri = Uri.parse(prevOriginalUrl)

                val currentQueryParamMap = mutableMapOf<String, String?>()
                val previousQueryParamMap = mutableMapOf<String, String?>()

                currentUri.queryParameterNames.forEach {
                    val value = currentUri.getQueryParameter(it)
                    if (it != uidKey)
                        currentQueryParamMap[it] = value
                }
                previousUri.queryParameterNames.forEach {
                    val value = currentUri.getQueryParameter(it)
                    if (it != uidKey)
                        previousQueryParamMap[it] = value
                }
                return currentQueryParamMap == previousQueryParamMap
            }
        }
        return false
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
            return Intent(context, BaseSimpleWebViewActivity::class.java).apply {
                putExtra(KEY_URL, url.encodeOnce())
                putExtra(KEY_TITLEBAR, showToolbar)
                putExtra(KEY_ALLOW_OVERRIDE, allowOverride)
                putExtra(KEY_NEED_LOGIN, needLogin)
                putExtra(KEY_TITLE, title)
            }
        }
    }

    object DeeplinkIntent {

        @DeepLink(ApplinkConst.WEBVIEW_PARENT_HOME)
        @JvmStatic
        fun getInstanceIntentAppLinkBackToHome(context: Context, extras: Bundle): TaskStackBuilder {
            val webUrl = extras.getString(KEY_URL, TokopediaUrl.getInstance().WEB)
            val taskStackBuilder = TaskStackBuilder.create(context)
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            taskStackBuilder.addNextIntent(RouteManager.getIntent(context, ApplinkConst.HOME))
            val destination = getStartIntent(context, webUrl)
            taskStackBuilder.addNextIntent(destination)
            return taskStackBuilder
        }

        @DeepLink(ApplinkConst.WEBVIEW, ApplinkConst.SellerApp.WEBVIEW, ApplinkConst.SELLER_INFO_DETAIL)
        @JvmStatic
        fun getInstanceIntentAppLink(context: Context, extras: Bundle): Intent {
            var webUrl = extras.getString(
                KEY_URL, TokopediaUrl.getInstance().WEB
            )
            var showToolbar: Boolean
            var needLogin: Boolean
            var allowOverride: Boolean

            try {
                showToolbar = extras.getBoolean(KEY_TITLEBAR, true)
            } catch (e: ParseException) {
                showToolbar = true
            }

            try {
                needLogin = extras.getBoolean(KEY_NEED_LOGIN, false)
            } catch (e: ParseException) {
                needLogin = false
            }

            try {
                allowOverride = extras.getBoolean(KEY_ALLOW_OVERRIDE, true)
            } catch (e: ParseException) {
                allowOverride = true
            }

            if (TextUtils.isEmpty(webUrl)) {
                webUrl = TokopediaUrl.Companion.getInstance().WEB
            }

            return getStartIntent(context, webUrl, showToolbar, allowOverride, needLogin)
        }
    }

}

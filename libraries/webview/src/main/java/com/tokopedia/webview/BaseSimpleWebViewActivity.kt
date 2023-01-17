package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.track.TrackApp
import com.tokopedia.webview.ext.decode
import com.tokopedia.webview.ext.encodeOnce

open class BaseSimpleWebViewActivity : BaseSimpleActivity() {

    protected lateinit var url: String
    var showTitleBar = true
    var pullToRefresh = false
        private set
    protected var allowOverride = true
    protected var needLogin = false
    protected var backPressedEnabled = true
    protected var backPressedMessage = ""
    var webViewTitle = ""
    var whiteListedDomains = WhiteListedDomains()

    override fun onCreate(savedInstanceState: Bundle?) {
        getWhiteListedDomains()
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
        url = ""
        intent.extras?.run {
            url = getString(KEY_URL, "").decode()
            showTitleBar = getBoolean(KEY_TITLEBAR, true)
            allowOverride = getBoolean(KEY_ALLOW_OVERRIDE, true)
            needLogin = getBoolean(KEY_NEED_LOGIN, false)
            pullToRefresh = getBoolean(KEY_PULL_TO_REFRESH, false)
            webViewTitle = getString(KEY_TITLE, DEFAULT_TITLE)
        }
        intent.data?.let { uri ->
            url = WebViewHelper.getEncodedUrlCheckSecondUrl(
                uri,
                url
            )

            showTitleBar = uri.getQueryParameter(KEY_TITLEBAR)?.toBoolean() ?: showTitleBar
            allowOverride = uri.getQueryParameter(KEY_ALLOW_OVERRIDE)?.toBoolean() ?: allowOverride
            needLogin = uri.getQueryParameter(KEY_NEED_LOGIN)?.toBoolean() ?: needLogin
            pullToRefresh = uri.getQueryParameter(KEY_PULL_TO_REFRESH)?.toBoolean() ?: pullToRefresh
            webViewTitle = uri.getQueryParameter(KEY_TITLE) ?: webViewTitle

            trackCampaign(uri)
        }
        logWebViewApplink()
    }

    //track campaign in case there is utm/gclid in url
    fun trackCampaign(uri: Uri) {
        TrackApp.getInstance().gtm.sendCampaign(this, uri.toString(), screenName, false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        if (showTitleBar) {
            inflater.inflate(R.menu.menu_web_view, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_home) {
            RouteManager.route(this, ApplinkConst.HOME)
        } else if (item.itemId == R.id.menu_help) {
            RouteManager.route(this, ApplinkConst.CONTACT_US_NATIVE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getNewFragment(): Fragment {
        if (::url.isInitialized) {
            return createFragmentInstance()
        } else {
            this.finish()
            return Fragment()
        }
    }

    protected open fun createFragmentInstance(): Fragment {
        return BaseSessionWebViewFragment.newInstance(url, needLogin, allowOverride, pullToRefresh)
    }

    override fun onResume() {
        super.onResume()
        reloadWebViewIfNeeded()
    }

    private fun reloadWebViewIfNeeded() {
        val needReload = try {
            PersistentCacheManager.instance.get(
                KEY_CACHE_RELOAD_WEBVIEW,
                Int::class.javaPrimitiveType!!,
                0
            ) == 1
        } catch (e: Exception) {
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
        //special behavior to handle finish if the url is paylater AN-34892
        if (f is BaseSessionWebViewFragment) {
            val currentUrl = f.webView?.url
            if (currentUrl != null &&
                (currentUrl.contains("/paylater/acquisition/status") ||
                        currentUrl.contains("/paylater/thank-you"))
            ) {
                this.finish()
                return
            }
        }
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
            if (backPressedEnabled) {
                val backUrl = getBackUrlFromQueryParameters()
                if (backUrl.isNullOrEmpty()) {
                    goPreviousActivity()
                } else {
                    val intent = Intent().apply { putExtra(KEY_BACK_URL, backUrl) }
                    setResult(RESULT_OK, intent)
                    performSafeFinish()
                }
            } else {
                showOnBackPressedDisabledMessage()
            }
        }
    }

    private fun getBackUrlFromQueryParameters(): String? {
        fragment?.let {
            if (fragment is BaseSessionWebViewFragment) {
                val currentUrl = (fragment as BaseWebViewFragment).webView?.url.orEmpty()
                if (currentUrl.isEmpty()) return null
                val uri = Uri.parse(currentUrl)
                val backUrl: String?
                try {
                    backUrl = uri.getQueryParameter(KEY_BACK_URL)
                    if (!backUrl.isNullOrEmpty()) {
                        return backUrl
                    }
                } catch (e: Exception) {
                    return null
                }

            }
            return null
        } ?: kotlin.run { return null }
    }

    fun setOnWebViewPageFinished() {
        val uri = intent.data
        uri?.let {
            backPressedEnabled = it.getBooleanQueryParameter(KEY_BACK_PRESSED_ENABLED, true)
            val message = it.getQueryParameter(KEY_BACK_PRESSED_MESSAGE)
            backPressedMessage = if (!message.isNullOrBlank()) {
                message
            } else {
                getString(R.string.webview_on_back_pressed_disabled_message)
            }
        }
    }

    fun enableBackButton() {
        val f = fragment
        if (f is BaseWebViewFragment && !f.webView.canGoBack()) {
            backPressedEnabled = true
        }
    }

    fun goPreviousActivity() {
        if (isTaskRoot || intent.data?.pathSegments?.joinToString() == ApplinkConst.WEBVIEW_PARENT_HOME_HOST) {
            RouteManager.route(this, ApplinkConst.HOME)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun performSafeFinish() {
        if (isTaskRoot || intent.data?.pathSegments?.joinToString() == ApplinkConst.WEBVIEW_PARENT_HOME_HOST) {
            RouteManager.route(this, ApplinkConst.HOME)
        }
        finish()
    }


    private fun checkForSameUrlInPreviousIndex(webView: WebView): Boolean {
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

    private fun showOnBackPressedDisabledMessage() {
        if (backPressedMessage.isNotBlank()) {
            Toast.makeText(this, backPressedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun logWebViewApplink() {
        val domain = getDomainName(url)
        if (domain.isNotEmpty()) {
            val baseDomain = getBaseDomain(domain)
            if (!baseDomain.equals(TOKOPEDIA_DOMAIN, ignoreCase = true)) {
                if (!isDomainWhitelisted(baseDomain) && whiteListedDomains.isEnabled) {
                    ServerLogger.log(
                        Priority.P1,
                        "WEBVIEW_OPENED",
                        mapOf("type" to "browser", "domain" to domain, "url" to url)
                    )
                    redirectToNativeBrowser()
                    return
                }
                ServerLogger.log(
                    Priority.P1,
                    "WEBVIEW_OPENED",
                    mapOf("type" to "webview", "domain" to domain, "url" to url)
                )
            }
        }
    }

    private fun redirectToNativeBrowser() {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
            finish()
        } catch (th: Throwable) {
            val messageMap: MutableMap<String, String> = HashMap()
            messageMap["type"] = "webview"
            messageMap["url"] = url
            ServerLogger.log(Priority.P1, "WRONG_DEEPLINK", messageMap)
        }
    }

    fun isDomainWhitelisted(domain: String): Boolean {
        if (whiteListedDomains.isEnabled) {
            whiteListedDomains.domains.forEach {
                if (it.contains(domain)) {
                    return true
                }
            }
            return false
        }
        return false
    }

    private fun getWhiteListedDomains() {
        try {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(this.applicationContext)
            val whiteListedDomainsCsv = firebaseRemoteConfig.getString(APP_WHITELISTED_DOMAINS_URL)
            if (whiteListedDomainsCsv.isNotBlank()) {
                whiteListedDomains =
                    Gson().fromJson(whiteListedDomainsCsv, WhiteListedDomains::class.java)
            }
        } catch (e: Exception) {
            whiteListedDomains = WhiteListedDomains()

            if (!GlobalConfig.isAllowDebuggingTools()) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    fun getBaseDomain(host: String): String {
        val split = host.split('.')
        return if (split.size > 2) split[1] else split[0]
    }

    private fun getDomainName(url: String): String {
        return Uri.parse(url).host ?: ""
    }

    companion object {

        const val TOKOPEDIA_DOMAIN = "tokopedia"
        const val APP_WHITELISTED_DOMAINS_URL = "ANDROID_WEBVIEW_WHITELIST_DOMAIN"
        const val KEY_BACK_URL = "back_url"

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

}

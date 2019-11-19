package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.app.TaskStackBuilder
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
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
    private var showTitleBar = true
    private var allowOverride = true
    private var needLogin = false
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        init(intent)
        if (!::url.isInitialized || url.isEmpty()) {
            finish()
        }
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun setupToolbar() {
        if (showTitleBar) {
            updateTitle(title)
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
            title = getString(KEY_TITLE, DEFAULT_TITLE)
        }
        intent.data?.run {
            url = getEncodedParameterUrl(this)
            showTitleBar = getQueryParameter(KEY_TITLEBAR)?.toBoolean() ?: true
            allowOverride = getQueryParameter(KEY_ALLOW_OVERRIDE)?.toBoolean() ?: true
            needLogin = getQueryParameter(KEY_NEED_LOGIN)?.toBoolean() ?: false
            title = getQueryParameter(KEY_TITLE) ?: DEFAULT_TITLE
        }
    }

    /**
     * This function is to get the url from the Uri
     * Example:
     * Input: tokopedia://webview?url=http://www.tokopedia.com/help
     * Output:http://www.tokopedia.com/help
     *
     * Input: tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fhelp%2F
     * Output:http://www.tokopedia.com/help
     *
     * Input: tokopedia://webview?url=https://js.tokopedia.com?url=http://www.tokopedia.com/help
     * Output:https://js.tokopedia.com?url=https%3A%2F%2Fwww.tokopedia.com%2Fhelp%2F
     *
     * Input: tokopedia://webview?url=https://js.tokopedia.com?url=http://www.tokopedia.com/help?id=4&target=5&title=3
     * Output:https://js.tokopedia.com?target=5&title=3&url=http%3A%2F%2Fwww.tokopedia.com%2Fhelp%3Fid%3D4%26target%3D5%26title%3D3
     */
    private fun getEncodedParameterUrl(intentUri: Uri): String {
        val query = intentUri.query
        return if (query != null && query.contains("$KEY_URL=")) {
            url = query.substringAfter("$KEY_URL=").decode()
            if (!url.contains("$KEY_URL=")) {
                return url
            }
            val url2 = url.substringAfter("$KEY_URL=")
            if (url2.isNotEmpty()) {
                val url2BeforeAnd = url2.substringBefore("&")
                val uriFromUrl = Uri.parse(url.replaceFirst("$KEY_URL=$url2BeforeAnd", "")
                    .replaceFirst("&&", "&").replaceFirst("?&", "&"))
                uriFromUrl.buildUpon()
                    .appendQueryParameter(KEY_URL, url2.encodeOnce())
                    .build().toString()
            } else {
                url
            }
        } else {
            ""
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
        return BaseSessionWebViewFragment.newInstance(url, needLogin, allowOverride)
    }

    override fun onResume() {
        super.onResume()
        if (PersistentCacheManager.instance.get(KEY_CACHE_RELOAD_WEBVIEW, Int::class.javaPrimitiveType!!, 0) == 1) {
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
            f.webView.goBack()
        } else {
            if (isTaskRoot) {
                RouteManager.route(this, ApplinkConst.HOME)
                finish()
            } else {
                super.onBackPressed()
            }
        }
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

        @DeepLink(ApplinkConst.WEBVIEW)
        @JvmStatic
        fun getInstanceIntentAppLink(context: Context, extras: Bundle): Intent {
            var webUrl = extras.getString(
                KEY_URL, TokopediaUrl.Companion.getInstance().WEB
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

            return getStartIntent(context, webUrl, showToolbar, needLogin, allowOverride)
        }
    }

}
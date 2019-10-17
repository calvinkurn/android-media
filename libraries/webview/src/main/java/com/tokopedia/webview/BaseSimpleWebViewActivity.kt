package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.TaskStackBuilder
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
            showTitleBar = getBoolean(KEY_SHOW_TITLEBAR, true)
            allowOverride = getBoolean(KEY_ALLOW_OVERRIDE, true)
            needLogin = getBoolean(KEY_NEED_LOGIN, false)
            title = getString(KEY_TITLE, DEFAULT_TITLE)
        }
        intent.data?.run {
            url = (getQueryParameter(KEY_URL) ?: "").decode()
            showTitleBar = getQueryParameter(KEY_SHOW_TITLEBAR)?.toBoolean() ?: true
            allowOverride = getQueryParameter(KEY_ALLOW_OVERRIDE)?.toBoolean() ?: true
            needLogin = getQueryParameter(KEY_NEED_LOGIN)?.toBoolean() ?: false
            title = getQueryParameter(KEY_TITLE) ?: DEFAULT_TITLE
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
            (fragment as? BaseSessionWebViewFragment)?.webView?.reload()
        }
    }

    override fun onBackPressed() {
        try {
            val fragment = fragment as? BaseSessionWebViewFragment
            if (fragment?.webView?.canGoBack() == true) {
                fragment.webView.goBack()
            } else {
                if (isTaskRoot) {
                    RouteManager.route(this, ApplinkConst.HOME)
                    finish()
                } else {
                    super.onBackPressed()
                }
            }
        } catch (e: Exception) {
            super.onBackPressed()
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
                putExtra(KEY_SHOW_TITLEBAR, showToolbar)
                putExtra(KEY_ALLOW_OVERRIDE, allowOverride)
                putExtra(KEY_NEED_LOGIN, needLogin)
                putExtra(KEY_TITLE, title)
            }
        }

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

    }

}
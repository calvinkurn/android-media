package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

open class BaseSimpleWebViewActivity : BaseSimpleActivity() {

    private lateinit var url: String
    private var needToolbar = DEFAULT_TOOLBAR_VISIBILITY

    override fun onCreate(savedInstanceState: Bundle?) {
        initField(savedInstanceState)
        validateField()

        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    private fun initField(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            assignField(savedInstanceState)
        } else {
            assignField(intent.extras)
        }
    }

    private fun assignField(bundle: Bundle?) {
        if (bundle == null) return
        with(bundle) {
            url = getString(KEY_URL, "")
            needToolbar = getBoolean(KEY_SHOW_TOOLBAR, DEFAULT_TOOLBAR_VISIBILITY)
        }
    }

    private fun validateField() {
        if (!::url.isInitialized || url.isEmpty()) {
            finish()
        }
    }

    private fun setupToolbar() {
        if (doesNotNeedToShowToolbar()) {
            hideToolbar()
        }
    }

    private fun doesNotNeedToShowToolbar(): Boolean = !needToolbar

    private fun hideToolbar() {
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        return BaseSessionWebViewFragment.newInstance(url)
    }

    companion object {
        private const val KEY_URL = "KEY_URL"
        private const val KEY_SHOW_TOOLBAR = "KEY_SHOW_TOOLBAR"

        private const val DEFAULT_TOOLBAR_VISIBILITY = true

        @JvmStatic
        fun getStartIntent(
                context: Context,
                url: String,
                showToolbar: Boolean = DEFAULT_TOOLBAR_VISIBILITY
        ): Intent {
            return Intent(context, BaseSimpleWebViewActivity::class.java).apply {
                putExtra(KEY_URL, url)
                putExtra(KEY_SHOW_TOOLBAR, showToolbar)
            }
        }
    }

}
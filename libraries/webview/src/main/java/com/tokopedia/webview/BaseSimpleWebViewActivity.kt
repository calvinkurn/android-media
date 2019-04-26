package com.tokopedia.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class BaseSimpleWebViewActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }

    override fun getNewFragment(): Fragment {
        if (doesNotHasIntentAndUrl()) {
            finish()
        }

        val url = getUrl()
        return BaseSessionWebViewFragment.newInstance(url)
    }

    private fun doesNotHasIntentAndUrl(): Boolean {
        return (intent == null) || (getUrl() == null)
    }

    private fun getUrl(): String? = intent.getStringExtra(INTENT_KEY_URL)

    companion object {
        const val INTENT_KEY_URL = "INTENT_KEY_URL"

        fun getStartIntent(context: Context, url: String): Intent {
            return Intent(context, BaseSimpleWebViewActivity::class.java).apply {
                putExtra(INTENT_KEY_URL, url)
            }
        }
    }

}
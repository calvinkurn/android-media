package com.tokopedia.ovop2p.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment

class OvoP2pWebViewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        updateTitle(intent.getStringExtra(TITLE))
        return BaseSessionWebViewFragment.newInstance(intent.getStringExtra(URL))
    }

    companion object {
        private val URL = "URL"
        private val TITLE = "TITLE"
        fun getWebViewIntent(context: Context, url: String, title: String): Intent {
            val intent = Intent(context, OvoP2pWebViewActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(TITLE, title)
            return intent
        }
    }
}

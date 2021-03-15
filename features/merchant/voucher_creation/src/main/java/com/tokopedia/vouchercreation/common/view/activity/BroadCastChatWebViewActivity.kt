package com.tokopedia.vouchercreation.common.view.activity

import android.content.Context
import android.content.Intent
import android.view.Menu
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLE

class BroadCastChatWebViewActivity : BaseSimpleWebViewActivity() {

    companion object {

        const val EXTRA_URL = "EXTRA_URL"

        fun createNewIntent(context: Context, url: String, title: String): Intent {
            val intent = Intent(context, BroadCastChatWebViewActivity::class.java)
            intent.putExtra(EXTRA_URL, url)
            intent.putExtra(KEY_TITLE, title)
            return intent
        }
    }

    override fun getNewFragment(): Fragment {
        val url = intent.getStringExtra(EXTRA_URL)
        return BaseSessionWebViewFragment.newInstance(url.toBlankOrString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }
}
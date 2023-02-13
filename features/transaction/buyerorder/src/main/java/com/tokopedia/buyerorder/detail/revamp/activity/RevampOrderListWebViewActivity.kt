package com.tokopedia.buyerorder.detail.revamp.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment

/**
 * created by @bayazidnasir on 19/8/2022
 */

class RevampOrderListWebViewActivity : BaseSimpleActivity(){

    companion object{
        private const val URL = "URL"
        private const val TITLE = "TITLE"

        fun getWebViewIntent(context: Context, url: String, title: String): Intent{
            return Intent(context, RevampOrderListWebViewActivity::class.java).apply {
                putExtra(URL, url)
                putExtra(TITLE, title)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        updateTitle(intent.getStringExtra(TITLE))
        return BaseSessionWebViewFragment.newInstance(intent.getStringExtra(URL) ?: "")
    }
}
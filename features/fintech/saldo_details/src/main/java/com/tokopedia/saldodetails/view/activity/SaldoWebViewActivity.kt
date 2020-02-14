package com.tokopedia.saldodetails.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment

class SaldoWebViewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return BaseSessionWebViewFragment.newInstance(intent.getStringExtra(URL))
    }

    companion object {

        private val URL = "URL"

        fun getWebViewIntent(context: Context, url: String?): Intent {
            val intent = Intent(context, SaldoWebViewActivity::class.java)
            intent.putExtra(URL, url)
            return intent
        }
    }
}

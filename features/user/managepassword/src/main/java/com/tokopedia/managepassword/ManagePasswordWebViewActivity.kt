package com.tokopedia.managepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL

class ManagePasswordWebViewActivity: BaseSimpleWebViewActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ManagePasswordWebViewFragment.instance(bundle)
    }

    companion object {
        fun createIntent(context: Context, url: String, titleBar: Boolean = true): Intent {
            val intent = Intent(context, ManagePasswordWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLEBAR, titleBar)
            return intent
        }
    }
}
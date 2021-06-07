package com.tokopedia.profilecompletion.common.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.webview.BaseSimpleWebViewActivity
import com.tokopedia.webview.KEY_TITLEBAR
import com.tokopedia.webview.KEY_URL

class ProfileSettingWebViewActivity : BaseSimpleWebViewActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent != null && intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ProfileSettingWebViewFragment.instance(bundle)
    }

    companion object {
        const val KEY_QUERY_PARAM = "ld"
        const val VALUE_QUERY_PARAM = "tokopedia-android-internal://success-change-emal"

        fun createIntent(context: Context, url: String, titleBar: Boolean = true): Intent {
            val intent = Intent(context, ProfileSettingWebViewFragment::class.java)
            intent.putExtra(KEY_URL, url)
            intent.putExtra(KEY_TITLEBAR, titleBar)
            return intent
        }
    }
}
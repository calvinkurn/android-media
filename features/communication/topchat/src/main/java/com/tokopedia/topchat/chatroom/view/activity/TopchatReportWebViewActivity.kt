package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.topchat.chatroom.view.fragment.TopchatReportWebViewFragment
import com.tokopedia.webview.*
import com.tokopedia.webview.ext.encodeOnce

class TopchatReportWebViewActivity: BaseSimpleWebViewActivity() {

    override fun createFragmentInstance(): Fragment {
        return TopchatReportWebViewFragment.newInstance(url, needLogin, allowOverride)
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
            return Intent(context, TopchatReportWebViewActivity::class.java).apply {
                putExtra(KEY_URL, url.encodeOnce())
                putExtra(KEY_TITLEBAR, showToolbar)
                putExtra(KEY_ALLOW_OVERRIDE, allowOverride)
                putExtra(KEY_NEED_LOGIN, needLogin)
                putExtra(KEY_TITLE, title)
            }
        }
    }
}
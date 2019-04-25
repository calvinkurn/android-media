package com.tokopedia.webview.chatreport.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.webview.BaseSessionWebViewFragment

class ChatReportActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        if (doesNotHasIntentAndReportUrl()) {
            finish()
        }

        val reportUrl = getReportUrl()
        return BaseSessionWebViewFragment.newInstance(reportUrl)
    }

    private fun doesNotHasIntentAndReportUrl(): Boolean {
        return (intent == null) || (getReportUrl() == null)
    }

    private fun getReportUrl(): String? = intent.getStringExtra(INTENT_KEY_REPORT_URL)

    companion object {
        val INTENT_KEY_REPORT_URL = "INTENT_KEY_REPORT_URL"

        fun getIntent(context: Context, messageId: String): Intent {
            val reportUrl = "https://m.tokopedia.com/chat/report/$messageId"
            return Intent(context, ChatReportActivity::class.java).apply {
                putExtra(INTENT_KEY_REPORT_URL, reportUrl)
            }
        }
    }

}
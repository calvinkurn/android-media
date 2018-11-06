package com.tokopedia.topads.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class TopAdsWebViewActivity: BaseSimpleActivity() {
    private var url = ""

    companion object {
        private const val PARAM_EXTRA_URL = "url"

        @JvmStatic
        fun createIntent(context: Context, url: String) = Intent(context, TopAdsWebViewActivity::class.java)
                .putExtra(PARAM_EXTRA_URL, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(PARAM_EXTRA_URL)){
            url = intent.getStringExtra(PARAM_EXTRA_URL)
        }
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = TopAdsWebViewFragment.newInstance(url)
}
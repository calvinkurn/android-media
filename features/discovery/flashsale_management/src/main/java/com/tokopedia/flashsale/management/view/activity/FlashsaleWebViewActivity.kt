package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.flashsale.management.view.fragment.FlashSaleWebViewFragment

class FlashsaleWebViewActivity: BaseSimpleActivity() {

    private var url = ""

    companion object {
        private const val PARAM_EXTRA_URL = "url"

        @JvmStatic
        fun createIntent(context: Context, url: String) = Intent(context, FlashsaleWebViewActivity::class.java)
                .putExtra(PARAM_EXTRA_URL, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (intent.hasExtra(PARAM_EXTRA_URL)){
            url = intent.getStringExtra(PARAM_EXTRA_URL)
        }
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = FlashSaleWebViewFragment.newInstance(url)
}
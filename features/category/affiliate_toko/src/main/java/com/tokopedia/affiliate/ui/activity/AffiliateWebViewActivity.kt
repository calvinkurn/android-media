package com.tokopedia.affiliate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.webview.BaseSessionWebViewFragment

class AffiliateWebViewActivity : BaseSimpleActivity() {

    companion object {
        private const val PARAM_URL = "url"
        fun createIntent(
            context: Context,
            url: String?
        ): Intent {
            val intent = Intent(context, AffiliateWebViewActivity::class.java)
            intent.putExtra(PARAM_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
    }

    override fun getNewFragment(): Fragment? {
        intent?.getStringExtra(PARAM_URL)?.let {
            return BaseSessionWebViewFragment.newInstance(it)
        }
        return null
    }

    private fun initInject() {
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
            .injectWebViewActivity(this)
    }
}

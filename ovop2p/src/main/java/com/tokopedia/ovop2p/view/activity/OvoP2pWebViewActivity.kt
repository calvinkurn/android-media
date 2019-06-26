package com.tokopedia.ovop2p.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.ovop2p.di.DaggerOvoP2pTransferComponent
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.webview.BaseSessionWebViewFragment

class OvoP2pWebViewActivity : BaseSimpleActivity(), HasComponent<OvoP2pTransferComponent> {

    private lateinit var ovoP2pTransferComponent: OvoP2pTransferComponent

    override fun getNewFragment(): Fragment {
        updateTitle(intent.getStringExtra(TITLE))
        return BaseSessionWebViewFragment.newInstance(intent.getStringExtra(URL))
    }

    private fun initInjector() {
        ovoP2pTransferComponent = DaggerOvoP2pTransferComponent.builder().baseAppComponent(
                (applicationContext as BaseMainApplication).baseAppComponent).build()
    }

    override fun getComponent(): OvoP2pTransferComponent {
        if (!::ovoP2pTransferComponent.isInitialized) {
            initInjector()
        }
        return ovoP2pTransferComponent
    }

    companion object {
        private val URL = "URL"
        private val TITLE = "TITLE"
        fun getWebViewIntent(context: Context, url: String, title: String): Intent {
            val intent = Intent(context, OvoP2pWebViewActivity::class.java)
            intent.putExtra(URL, url)
            intent.putExtra(TITLE, title)
            return intent
        }
    }
}

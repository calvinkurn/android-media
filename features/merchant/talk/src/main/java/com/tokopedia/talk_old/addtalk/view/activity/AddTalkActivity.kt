package com.tokopedia.talk_old.addtalk.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.feature.write.presentation.activity.TalkWriteActivity
import com.tokopedia.talk_old.addtalk.view.fragment.AddTalkFragment
import com.tokopedia.talk_old.common.di.DaggerTalkComponent
import com.tokopedia.talk_old.common.di.TalkComponent

class AddTalkActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {
    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object {
        const val EXTRA_PRODUCT_ID: String = "product_id"

        fun createIntent(context: Context,
                         productId: String,
                         source: String): Intent{
            val intent = Intent(context, AddTalkActivity::class.java)
            val bundle = Bundle()
            bundle.putString(AddTalkActivity.EXTRA_PRODUCT_ID, productId)
            bundle.putString(TalkConstants.PARAM_SOURCE, source)
            intent.putExtras(bundle)
            return intent
        }

    }

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var productId = ""
    private var source = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromAppLink()
        getAbTestPlatform()?.fetch(null)
        super.onCreate(savedInstanceState)
//        if (!useOldPage()) {
            startActivity(TalkWriteActivity.createIntent(this, productId.toIntOrZero()))
            finish()
            return
//        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putString(EXTRA_PRODUCT_ID, productId)
            bundle.putString(TalkConstants.PARAM_SOURCE, source)
            bundle.putAll(intent.extras)
        }

        return AddTalkFragment.newInstance(intent.extras ?: Bundle())
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    private fun getDataFromAppLink() {
        val uri = intent.data ?: return
        val productIdString = uri.getQueryParameter(TalkConstants.PARAM_PRODUCT_ID) ?: return
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString
        }
        val sourceQuery = uri.getQueryParameter(TalkConstants.PARAM_SOURCE) ?: return
        if(sourceQuery.isNotEmpty()) {
            this.source = sourceQuery
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(this.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun useOldPage(): Boolean {
        return getAbTestPlatform()?.getString(TalkConstants.AB_TEST_WRITE_KEY).equals(TalkConstants.WRITE_OLD_FLOW)
    }
}
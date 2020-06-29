package com.tokopedia.talk.feature.write.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.write.presentation.fragment.TalkWriteFragment

class TalkWriteActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    companion object {
        fun createIntent(context: Context, productId: Int): Intent {
            val intent = Intent(context, TalkWriteActivity::class.java)
            intent.putExtra(TalkConstants.PARAM_PRODUCT_ID, productId)
            return intent
        }
    }

    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        productId = intent.getIntExtra(TalkConstants.PARAM_PRODUCT_ID, productId)
        if(productId == 0) {
            getDataFromApplink()
        }
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkWriteFragment.createNewInstance(productId)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = TalkConstants.NO_SHADOW_ELEVATION
    }

    private fun getDataFromApplink() {
        val uri = intent.data ?: return
        val productIdString = uri.pathSegments[uri.pathSegments.size - 1] ?: return
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString.toIntOrZero()
        }
    }
}
package com.tokopedia.talk.feature.reading.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.talk.common.TalkConstants.NO_SHADOW_ELEVATION
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.presentation.fragment.TalkReadingFragment

class TalkReadingActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        getProductIdFromAppLink()
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkReadingFragment.createNewInstance(productId)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun getProductIdFromAppLink() {
        val uri = intent.data ?: return
        val productIdString = uri.lastPathSegment ?: return
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString.toIntOrZero()
        }
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = NO_SHADOW_ELEVATION
    }


}
package com.tokopedia.talk.feature.reply.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.TalkConstants
import com.tokopedia.talk.common.di.DaggerTalkComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reply.presentation.fragment.TalkReplyFragment

class TalkReplyActivity : BaseSimpleActivity(), HasComponent<TalkComponent> {

    private var questionId = ""
    private var shopId = ""
    private var productId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getDataFromAppLink()
        super.onCreate(savedInstanceState)
        setUpToolBar()
    }

    override fun getNewFragment(): Fragment? {
        return TalkReplyFragment.createNewInstance(questionId, shopId, productId)
    }

    override fun getComponent(): TalkComponent {
        return DaggerTalkComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun setUpToolBar() {
        supportActionBar?.elevation = TalkConstants.NO_SHADOW_ELEVATION
    }

    private fun getDataFromAppLink() {
        val uri = intent.data ?: return
        val questionIdString = uri.pathSegments[uri.pathSegments.size - 3] ?: return
        if (questionIdString.isNotEmpty()) {
            this.questionId = questionIdString
        }
        val shopIdString = uri.pathSegments[uri.pathSegments.size - 2] ?: return
        if (shopIdString.isNotEmpty()) {
            this.shopId = shopIdString
        }
        val productIdString = uri.lastPathSegment ?: return
        if (productIdString.isNotEmpty()) {
            this.productId = productIdString
        }
    }
}
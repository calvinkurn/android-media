package com.tokopedia.developer_options.presentation.feedbackpage.ui

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.developer_options.presentation.feedbackpage.di.DaggerFeedbackPageComponent
import com.tokopedia.developer_options.presentation.feedbackpage.di.FeedbackPageComponent
import com.tokopedia.developer_options.presentation.feedbackpage.utils.EXTRA_URI_IMAGE

class FeedbackPageActivity : BaseSimpleActivity(), HasComponent<FeedbackPageComponent> {

    override fun getNewFragment(): Fragment? {
        var uriData: Uri?
        val uri = intent.data
        uriData = if (uri != null) {
            intent.getParcelableExtra(EXTRA_URI_IMAGE)
        } else {
            null
        }
        return FeedbackPageFragment.newInstance(uriData)
    }

    override fun getComponent(): FeedbackPageComponent {
        return DaggerFeedbackPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}
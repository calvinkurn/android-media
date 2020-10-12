package com.tokopedia.developer_options.presentation.feedbackpage.ui.feedbackpage

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.developer_options.presentation.feedbackpage.di.DaggerFeedbackPageComponent
import com.tokopedia.developer_options.presentation.feedbackpage.di.FeedbackPageComponent

class FeedbackPageActivity : BaseSimpleActivity(), HasComponent<FeedbackPageComponent> {

    override fun getNewFragment(): Fragment? {
        var uriData: Uri?
        val uri = intent.getParcelableExtra<Uri>("EXTRA_URI_IMAGE")?: null
        val classname = intent.getStringExtra("EXTRA_IS_CLASS_NAME")?: ""
        return FeedbackPageFragment.newInstance(uri, classname)
    }

    override fun getComponent(): FeedbackPageComponent {
        return DaggerFeedbackPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}
package com.tokopedia.feedback_form.feedbackpage.ui.feedbackpage

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.feedback_form.feedbackpage.di.DaggerFeedbackPageComponent
import com.tokopedia.feedback_form.feedbackpage.di.FeedbackPageComponent
import com.tokopedia.feedback_form.feedbackpage.util.EXTRA_IS_CLASS_NAME
import com.tokopedia.feedback_form.feedbackpage.util.EXTRA_IS_FROM_SCREENSHOT
import com.tokopedia.feedback_form.feedbackpage.util.EXTRA_URI_IMAGE

class FeedbackPageActivity : BaseSimpleActivity(), HasComponent<FeedbackPageComponent> {

    override fun getNewFragment(): Fragment? {
        var uriData: Uri?
        val uri = intent.getParcelableExtra<Uri>(EXTRA_URI_IMAGE)?: null
        val classname = intent.getStringExtra(EXTRA_IS_CLASS_NAME)?: ""
        val isFromScreenshot = intent.getBooleanExtra(EXTRA_IS_FROM_SCREENSHOT, false)
        return FeedbackPageFragment.newInstance(uri, classname, isFromScreenshot)
    }

    override fun getComponent(): FeedbackPageComponent {
        return DaggerFeedbackPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}
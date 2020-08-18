package com.tokopedia.developer_options.presentation.feedbackpage

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FeedbackPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return FeedbackPageFragment()
    }

}
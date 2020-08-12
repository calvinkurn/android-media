package com.tokopedia.developer_options.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.developer_options.presentation.fragment.FeedbackPageFragment

class FeedbackPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return FeedbackPageFragment()
    }

}
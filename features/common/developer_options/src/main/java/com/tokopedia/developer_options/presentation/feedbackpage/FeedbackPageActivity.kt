package com.tokopedia.developer_options.presentation.feedbackpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FeedbackPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var fragment: FeedbackPageFragment? = null
        if (intent.extras != null) {
            val bundle = intent.extras
            fragment = FeedbackPageFragment.newInstance(bundle?: Bundle())
        }
        return fragment
    }

}
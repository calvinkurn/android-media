package com.tokopedia.developer_options.presentation.feedbackpage

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FeedbackPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var fragment: FeedbackPageFragment? = null
        if (intent.extras != null) {
            val uri: Uri = intent.getParcelableExtra("EXTRA_URI_IMAGE")
            fragment = FeedbackPageFragment.newInstance(uri)
        }
        return fragment
    }

}
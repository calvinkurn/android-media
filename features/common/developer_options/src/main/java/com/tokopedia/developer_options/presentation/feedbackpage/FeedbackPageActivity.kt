package com.tokopedia.developer_options.presentation.feedbackpage

import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class FeedbackPageActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        var uriData: Uri?
        val uri = intent.data
        uriData = if (uri != null) {
            intent.getParcelableExtra("EXTRA_URI_IMAGE")
        } else {
            null
        }
        return FeedbackPageFragment.newInstance(uriData)
    }

}
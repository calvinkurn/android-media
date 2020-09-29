package com.tokopedia.developer_options.presentation.feedbackpage.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.developer_options.presentation.feedbackpage.ui.fragment.DrawOnPictureFragment

class DrawOnPictureActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = DrawOnPictureFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}

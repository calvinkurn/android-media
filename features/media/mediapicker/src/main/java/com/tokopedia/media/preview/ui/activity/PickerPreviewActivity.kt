package com.tokopedia.media.preview.ui.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.media.preview.ui.fragment.PickerPreviewFragment

class PickerPreviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return PickerPreviewFragment()
    }

}
package com.tokopedia.media.picker.common.ui.activity

import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity

class TestPreviewActivity : PickerPreviewActivity() {

    override fun initInjector() {
        PickerTest.previewComponent?.inject(this)
    }

}
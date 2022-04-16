package com.tokopedia.media.picker.common.ui.activity

import com.tokopedia.media.picker.common.ui.TestPickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.activity.main.PickerActivity

class TestPickerActivity : PickerActivity() {

    override fun createFragmentFactory(): PickerFragmentFactory {
        return TestPickerFragmentFactory()
    }

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

}
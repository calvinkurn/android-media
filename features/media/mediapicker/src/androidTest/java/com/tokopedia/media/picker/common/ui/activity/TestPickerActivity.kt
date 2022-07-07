package com.tokopedia.media.picker.common.ui.activity

import android.os.Bundle
import com.tokopedia.media.picker.common.ui.TestPickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.activity.picker.PickerActivity

class TestPickerActivity : PickerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupQueryAndUIConfigBuilder()
        super.onCreate(savedInstanceState)
    }

    private fun setupQueryAndUIConfigBuilder() {
        val data = intent?.data ?: return

        // TODO: change with intent data from [RESULT_PICKER]
//        PickerUiConfig.setupQueryPage(data)
//        PickerUiConfig.setupQueryMode(data)
//        PickerUiConfig.setupQuerySelectionType(data)
//        PickerUiConfig.setupQueryLandingPageIndex(data)
    }

    override fun createFragmentFactory(): PickerFragmentFactory {
        return TestPickerFragmentFactory()
    }

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

}
package com.tokopedia.picker.fake.ui.activity

import android.os.Bundle
import com.tokopedia.picker.fake.ui.TestPickerFragmentFactory
import com.tokopedia.picker.ui.PickerFragmentFactory
import com.tokopedia.picker.ui.PickerTest
import com.tokopedia.picker.ui.PickerUiConfig
import com.tokopedia.picker.ui.activity.main.PickerActivity

class TestPickerActivity : PickerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupQueryAndUIConfigBuilder()
        super.onCreate(savedInstanceState)
    }

    private fun setupQueryAndUIConfigBuilder() {
        val data = intent?.data ?: return

        PickerUiConfig.setupQueryPage(data)
        PickerUiConfig.setupQueryMode(data)
        PickerUiConfig.setupQuerySelectionType(data)
    }

    override fun createFragmentFactory(): PickerFragmentFactory {
        return TestPickerFragmentFactory()
    }

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

}
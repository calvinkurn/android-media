package com.tokopedia.media.picker.common.ui.activity

import android.os.Bundle
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.PreviewTest
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.picker.common.EXTRA_PICKER_PARAM
import com.tokopedia.picker.common.PickerParam

class TestPreviewActivity : PickerPreviewActivity() {

    override fun initInjector() {
        PreviewTest.previewComponent?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        setParamToManager()
        super.onCreate(savedInstanceState)
    }

    fun setParamToManager() {
        val pickerParam = intent?.getParcelableExtra(EXTRA_PICKER_PARAM)?: PickerParam()

        // get data from uri query parameter
        intent?.data?.let {
            PickerUiConfig.getStartPageIndex(it)
        }

        // set the picker param as cache
        param.setParam(pickerParam)
    }
}
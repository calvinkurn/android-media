package com.tokopedia.media.picker.common.ui.fragment

import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment

class TestCameraFragment : CameraFragment() {

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

    companion object {
        fun create(): TestCameraFragment {
            return TestCameraFragment()
        }
    }

}
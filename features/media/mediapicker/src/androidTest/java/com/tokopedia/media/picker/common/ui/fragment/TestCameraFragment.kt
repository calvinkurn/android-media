package com.tokopedia.media.picker.common.ui.fragment

import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment

class TestCameraFragment : CameraFragment() {
    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

    override fun onThumbnailLoaded() {
        super.onThumbnailLoaded()
        CameraPageTest.Robot.resumeThread()
    }

    companion object {
        fun create(): TestCameraFragment {
            return TestCameraFragment()
        }
    }
}
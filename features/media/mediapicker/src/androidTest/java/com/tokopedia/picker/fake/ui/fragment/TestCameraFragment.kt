package com.tokopedia.picker.fake.ui.fragment

import com.tokopedia.picker.ui.fragment.camera.CameraFragment

class TestCameraFragment : CameraFragment() {

    override fun initInjector() {

    }

    companion object {
        fun create(): TestCameraFragment {
            return TestCameraFragment()
        }
    }

}
package com.tokopedia.media.picker.common.ui.fragment

import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment

class TestCameraFragment : CameraFragment() {

    override fun initInjector() {

    }

    companion object {
        fun create(): TestCameraFragment {
            return TestCameraFragment()
        }
    }

}
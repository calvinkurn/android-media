package com.tokopedia.media.picker.common.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import javax.inject.Inject

class TestCameraFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    param: ParamCacheManager,
    cameraAnalytics: CameraAnalytics,
) : CameraFragment(
    viewModelFactory,
    param,
    cameraAnalytics
) {

    override fun onThumbnailLoaded() {
        super.onThumbnailLoaded()
        CameraPageTest.Robot.decrement()
    }

}
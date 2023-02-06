package com.tokopedia.media.picker.common.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.picker.common.cache.PickerCacheManager
import javax.inject.Inject

class TestCameraFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    param: PickerCacheManager,
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

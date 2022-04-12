package com.tokopedia.media.picker.common.ui

import androidx.fragment.app.Fragment
import com.tokopedia.media.picker.common.ui.fragment.TestCameraFragment
import com.tokopedia.media.picker.common.ui.fragment.TestGalleryFragment
import com.tokopedia.media.picker.common.ui.fragment.TestPermissionFragment
import com.tokopedia.media.picker.ui.PickerFragmentFactory

class TestPickerFragmentFactory : PickerFragmentFactory {

    override fun permissionFragment(): Fragment {
        return TestPermissionFragment()
    }

    override fun cameraFragment(): Fragment {
        return TestCameraFragment()
    }

    override fun galleryFragment(): Fragment {
        return TestGalleryFragment()
    }

}
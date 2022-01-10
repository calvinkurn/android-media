package com.tokopedia.picker.fake.ui

import androidx.fragment.app.Fragment
import com.tokopedia.picker.fake.ui.fragment.TestCameraFragment
import com.tokopedia.picker.fake.ui.fragment.TestGalleryFragment
import com.tokopedia.picker.fake.ui.fragment.TestPermissionFragment
import com.tokopedia.picker.ui.PickerFragmentFactory

class TestPickerFragmentFactory : PickerFragmentFactory {

    override fun permissionBoardingFragment(): Fragment {
        return TestPermissionFragment()
    }

    override fun cameraFragment(): Fragment {
        return TestCameraFragment()
    }

    override fun galleryFragment(): Fragment {
        return TestGalleryFragment()
    }

}
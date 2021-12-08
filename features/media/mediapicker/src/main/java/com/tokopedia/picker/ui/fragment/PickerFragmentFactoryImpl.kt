package com.tokopedia.picker.ui.fragment

import androidx.fragment.app.Fragment
import com.tokopedia.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.picker.ui.fragment.permission.PermissionFragment
import com.tokopedia.picker.ui.fragment.picker.PickerFragment

class PickerFragmentFactoryImpl : PickerFragmentFactory {

    override fun permissionBoardingFragment(): Fragment {
        return PermissionFragment()
    }

    override fun cameraFragment(): Fragment {
        return CameraFragment()
    }

    override fun pickerFragment(): Fragment {
        return PickerFragment()
    }

    override fun galleryFragment(): Fragment {
        return GalleryFragment()
    }

}
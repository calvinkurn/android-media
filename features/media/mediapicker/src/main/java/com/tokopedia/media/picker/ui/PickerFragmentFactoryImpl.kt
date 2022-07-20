package com.tokopedia.media.picker.ui

import androidx.fragment.app.Fragment
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment

class PickerFragmentFactoryImpl : PickerFragmentFactory {

    override fun permissionFragment(): Fragment {
        return PermissionFragment()
    }

    override fun cameraFragment(): Fragment {
        return CameraFragment()
    }

    override fun galleryFragment(): Fragment {
        return GalleryFragment()
    }

}
package com.tokopedia.media.picker.common.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.picker.common.ui.fragment.TestCameraFragment
import com.tokopedia.media.picker.common.ui.fragment.TestGalleryFragment
import com.tokopedia.media.picker.common.ui.fragment.TestPermissionFragment
import com.tokopedia.media.picker.ui.PickerFragmentFactory
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment

class TestPickerFragmentFactory constructor(
    private val mFragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : PickerFragmentFactory {

    override val fragmentManager: FragmentManager
        get() = mFragmentManager

    override fun permissionFragment(): Fragment {
        return fragmentCreation(TestPermissionFragment::class.java.name) as TestPermissionFragment
    }

    override fun cameraFragment(): Fragment {
        return fragmentCreation(TestCameraFragment::class.java.name) as TestCameraFragment
    }

    override fun galleryFragment(): Fragment {
        return fragmentCreation(TestGalleryFragment::class.java.name) as TestGalleryFragment
    }

    private fun fragmentCreation(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }

}

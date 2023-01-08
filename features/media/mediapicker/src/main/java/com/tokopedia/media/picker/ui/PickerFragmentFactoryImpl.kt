package com.tokopedia.media.picker.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.picker.ui.fragment.camera.CameraFragment
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment

class PickerFragmentFactoryImpl constructor(
    private val fragmentManager: FragmentManager,
    private val classLoader: ClassLoader
) : PickerFragmentFactory {

    override fun permissionFragment(): Fragment {
        return fragmentCreation(
            PermissionFragment::class.java.name
        ) as PermissionFragment
    }

    override fun cameraFragment(): Fragment {
        return fragmentCreation(
            CameraFragment::class.java.name
        ) as CameraFragment
    }

    override fun galleryFragment(): Fragment {
        return fragmentCreation(
            GalleryFragment::class.java.name
        ) as GalleryFragment
    }

    private fun fragmentCreation(name: String): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, name)
    }

}
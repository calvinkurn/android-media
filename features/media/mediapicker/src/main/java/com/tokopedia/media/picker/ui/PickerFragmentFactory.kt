package com.tokopedia.media.picker.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface PickerFragmentFactory {
    val fragmentManager: FragmentManager

    fun permissionFragment(): Fragment
    fun cameraFragment(): Fragment
    fun galleryFragment(): Fragment
}

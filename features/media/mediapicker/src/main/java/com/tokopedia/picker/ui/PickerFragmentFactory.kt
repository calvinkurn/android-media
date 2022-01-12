package com.tokopedia.picker.ui

import androidx.fragment.app.Fragment

interface PickerFragmentFactory {
    fun permissionFragment(): Fragment
    fun cameraFragment(): Fragment
    fun galleryFragment(): Fragment
}
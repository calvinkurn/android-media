package com.tokopedia.picker.ui.fragment

import androidx.fragment.app.Fragment

interface PickerFragmentFactory {
    fun permissionBoardingFragment(): Fragment
    fun pickerFragment(): Fragment
    fun cameraFragment(): Fragment
    fun galleryFragment(): Fragment
}
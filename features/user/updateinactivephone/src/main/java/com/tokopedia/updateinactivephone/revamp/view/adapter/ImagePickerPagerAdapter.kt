package com.tokopedia.updateinactivephone.revamp.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.imagepicker.picker.main.adapter.ImagePickerViewPagerAdapter
import com.tokopedia.imagepicker.picker.main.builder.ImagePickerBuilder
import com.tokopedia.updateinactivephone.revamp.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneCameraFragment

class ImagePickerPagerAdapter constructor(
        context: Context,
        fragmentManager: FragmentManager,
        imagePickerBuilder: ImagePickerBuilder,
        private val cameraViewMode: CameraViewMode?
) : ImagePickerViewPagerAdapter(context, fragmentManager, imagePickerBuilder) {

    override fun createCameraFragment(): Fragment {
        return cameraViewMode?.let {
            InactivePhoneCameraFragment.instance(it)
        } as Fragment
    }
}
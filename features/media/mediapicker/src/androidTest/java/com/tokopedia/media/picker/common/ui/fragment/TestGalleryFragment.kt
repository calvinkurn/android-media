package com.tokopedia.media.picker.common.ui.fragment

import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment

class TestGalleryFragment : GalleryFragment() {

    override fun initInjector() {
        PickerTest.pickerComponent?.inject(this)
    }

    companion object {
        fun create(): TestGalleryFragment {
            return TestGalleryFragment()
        }
    }

}
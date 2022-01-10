package com.tokopedia.picker.fake.ui.fragment

import com.tokopedia.picker.ui.PickerTest
import com.tokopedia.picker.ui.fragment.gallery.GalleryFragment

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
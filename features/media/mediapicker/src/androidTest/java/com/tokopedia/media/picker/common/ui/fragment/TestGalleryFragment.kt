package com.tokopedia.media.picker.common.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import javax.inject.Inject

class TestGalleryFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    param: ParamCacheManager,
    galleryAnalytics: GalleryAnalytics,
) : GalleryFragment(
    viewModelFactory,
    param,
    galleryAnalytics
)
package com.tokopedia.media.picker.common.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.ui.fragment.gallery.GalleryFragment
import com.tokopedia.picker.common.cache.PickerCacheManager
import javax.inject.Inject

class TestGalleryFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    param: PickerCacheManager,
    galleryAnalytics: GalleryAnalytics,
) : GalleryFragment(
    viewModelFactory,
    param,
    galleryAnalytics
)

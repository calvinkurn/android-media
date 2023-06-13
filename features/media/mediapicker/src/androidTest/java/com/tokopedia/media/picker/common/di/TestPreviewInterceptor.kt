package com.tokopedia.media.picker.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.picker.common.cache.PickerCacheManager

import javax.inject.Inject

class TestPreviewInterceptor {
    @Inject
    lateinit var param: PickerCacheManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory
}

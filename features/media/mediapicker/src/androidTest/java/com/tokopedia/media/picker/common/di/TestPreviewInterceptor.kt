package com.tokopedia.media.picker.common.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.common.utils.ParamCacheManager
import javax.inject.Inject

class TestPreviewInterceptor {
    @Inject
    lateinit var param: ParamCacheManager

    @Inject
    lateinit var factory: ViewModelProvider.Factory
}
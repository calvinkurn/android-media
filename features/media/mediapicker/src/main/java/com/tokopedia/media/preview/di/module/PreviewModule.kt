package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.picker.common.ParamCacheManager
import dagger.Module
import dagger.Provides

@Module
class PreviewModule {

    @Provides
    @PreviewScope
    fun providePreviewAnalytics(): PreviewAnalytics {
        return PreviewAnalyticsImpl()
    }

    @Provides
    @PreviewScope
    fun provideParamCacheManager(
        @ApplicationContext context: Context
    ): ParamCacheManager {
        return ParamCacheManager(context)
    }

}
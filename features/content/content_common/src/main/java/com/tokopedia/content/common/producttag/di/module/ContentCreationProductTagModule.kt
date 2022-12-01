package com.tokopedia.content.common.producttag.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on May 23, 2022
 */
@Module
class ContentCreationProductTagModule {

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }
}
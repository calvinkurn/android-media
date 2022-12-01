package com.tokopedia.content.common.producttag.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@Module
class ContentCreationProductTagTestModule(
    private val mockRepo: ProductTagRepository,
) {

    @Provides
    fun bindProductTagRepository(): ProductTagRepository = mockRepo

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }
}
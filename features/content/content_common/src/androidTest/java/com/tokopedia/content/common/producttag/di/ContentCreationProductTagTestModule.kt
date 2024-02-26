package com.tokopedia.content.common.producttag.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.trackingoptimizer.TrackingQueue
import dagger.Module
import dagger.Provides

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
@Module
class ContentCreationProductTagTestModule(
        private val mockRepo: com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository,
) {

    @Provides
    fun bindProductTagRepository(): com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository = mockRepo

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context): TrackingQueue {
        return TrackingQueue(context)
    }
}

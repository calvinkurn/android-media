package com.tokopedia.play.broadcaster.setup.di

import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import dagger.Module
import dagger.Provides
import io.mockk.mockk

/**
 * Created By : Jonathan Darwin on April 12, 2023
 */
@Module
class PlayBroadcastRepositoryTestModule(
    private val mockRepo: PlayBroadcastRepository,
    private val mockContentProductPickerSGCRepo: ContentProductPickerSellerRepository = mockk(relaxed = true),
    private val mockProductPickerCommonRepo: ProductPickerSellerCommonRepository = mockk(relaxed = true),
) {

    @Provides
    @ActivityRetainedScope
    fun providePlayBroadcastRepository(): PlayBroadcastRepository = mockRepo

    @Provides
    @ActivityRetainedScope
    fun provideContentProductPickerSGCRepository(): ContentProductPickerSellerRepository = mockContentProductPickerSGCRepo

    @Provides
    @ActivityRetainedScope
    fun provideProductPickerSellerCommonRepository(): ProductPickerSellerCommonRepository = mockProductPickerCommonRepo
}

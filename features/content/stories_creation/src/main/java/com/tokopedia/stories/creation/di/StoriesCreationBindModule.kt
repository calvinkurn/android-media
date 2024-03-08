package com.tokopedia.stories.creation.di

import com.tokopedia.content.common.util.bottomsheet.ContentDialogCustomizer
import com.tokopedia.content.common.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.content.product.picker.seller.analytic.ContentPinnedProductAnalytic
import com.tokopedia.content.product.picker.seller.analytic.ContentProductPickerSellerAnalytic
import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalytic
import com.tokopedia.stories.creation.analytic.StoriesCreationAnalyticImpl
import com.tokopedia.stories.creation.analytic.product.seller.StoriesCreationPinnedProductAnalytic
import com.tokopedia.stories.creation.analytic.product.seller.StoriesCreationProductSellerAnalytic
import com.tokopedia.stories.creation.data.StoriesCreationProductRepositoryImpl
import com.tokopedia.stories.creation.data.StoriesCreationRepositoryImpl
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on September 05, 2023
 */
@Module
abstract class StoriesCreationBindModule {

    /** Analytic */
    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationAnalytic(storiesCreationAnalytic: StoriesCreationAnalyticImpl): StoriesCreationAnalytic

    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationProductSellerAnalytic(storiesCreationProductSellerAnalytic: StoriesCreationProductSellerAnalytic): ContentProductPickerSellerAnalytic

    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationPinnedProductAnalytic(storiesCreationPinnedProductAnalytic: StoriesCreationPinnedProductAnalytic): ContentPinnedProductAnalytic

    /** Repository */
    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationRepository(repository: StoriesCreationRepositoryImpl): StoriesCreationRepository

    @Binds
    @StoriesCreationScope
    abstract fun bindStoriesCreationProductRepository(storiesCreationProductRepository: StoriesCreationProductRepositoryImpl): ContentProductPickerSellerRepository


    /** Util */
    @Binds
    @StoriesCreationScope
    abstract fun bindNavigationBarColorDialogCustomizer(customizer: NavigationBarColorDialogCustomizer): ContentDialogCustomizer
}

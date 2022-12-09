package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class InspirationCarouselModule {

    @Binds
    @SearchScope
    abstract fun provideInspirationCarouselDynamicProductView(
        delegate: InspirationCarouselDynamicProductViewDelegate
    ): InspirationCarouselDynamicProductView

    @Binds
    @SearchScope
    abstract fun provideInspirationCarouselView(
        delegate: InspirationCarouselViewDelegate
    ): InspirationCarouselView

    @Binds
    @SearchScope
    abstract fun provideInspirationCarouselPresenter(
        delegate: InspirationCarouselPresenterDelegate
    ): InspirationCarouselPresenter
}

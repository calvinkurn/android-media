package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class InspirationProductModule {

    @Binds
    abstract fun provideInspirationProductPresenter(
        inspirationProductPresenterDelegate: InspirationProductPresenterDelegate
    ): InspirationProductPresenter

    @Binds
    @SearchScope
    abstract fun provideInspirationCarouselDynamicProductView(
        delegate: InspirationProductViewDelegate
    ): InspirationProductView
}

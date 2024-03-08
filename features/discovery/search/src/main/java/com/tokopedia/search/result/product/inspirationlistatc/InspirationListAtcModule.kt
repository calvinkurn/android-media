package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcViewDelegate
import dagger.Binds
import dagger.Module

@Module
abstract class InspirationListAtcModule {

    @SearchScope
    @Binds
    abstract fun provideInspirationListAtcPresenter(provider: InspirationListAtcPresenterDelegate): InspirationListAtcPresenter

    @SearchScope
    @Binds
    abstract fun provideInspirationListAtcView(provider: InspirationListAtcViewDelegate): InspirationListAtcView

    @SearchScope
    @Binds
    abstract fun provideInspirationListPostAtcView(provider: InspirationListPostAtcViewDelegate): InspirationListPostAtcView
}

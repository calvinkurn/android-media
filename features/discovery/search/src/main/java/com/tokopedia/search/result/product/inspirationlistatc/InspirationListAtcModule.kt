package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.di.scope.SearchScope
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
}

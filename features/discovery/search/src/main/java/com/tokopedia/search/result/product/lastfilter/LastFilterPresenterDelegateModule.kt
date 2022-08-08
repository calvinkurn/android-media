package com.tokopedia.search.result.product.lastfilter

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class LastFilterPresenterDelegateModule {

    @SearchScope
    @Binds
    abstract fun provideLastFilterPresenter(provider: LastFilterPresenterDelegate): LastFilterPresenter
}
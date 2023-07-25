package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class InspirationKeywordModule {

    @Binds
    @SearchScope
    abstract fun provideInspirationKeywordView(
        broadMatchViewDelegate: InspirationKeywordViewDelegate
    ): InspirationKeywordView

    @Binds
    abstract fun provideInspirationKeywordPresenter(
        broadMatchPresenterDelegate: InspirationKeywordPresenterDelegate
    ): InspirationKeywordPresenter
}

package com.tokopedia.search.result.product.semlessproduct.seamlesskeywordoptions

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class InspirationKeywordModule {

    @Binds
    @SearchScope
    abstract fun provideInspirationKeywordView(
        inspirationKeywordViewDelegate: InspirationKeywordViewDelegate
    ): InspirationKeywordView

    @Binds
    abstract fun provideInspirationKeywordPresenter(
        inspirationKeywordPresenterDelegate: InspirationKeywordPresenterDelegate
    ): InspirationKeywordPresenter
}

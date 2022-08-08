package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ProductListPresenterModule {

    @SearchScope
    @Binds
    abstract fun provideProductListPresenter(presenter: ProductListPresenter): ProductListSectionContract.Presenter

    @SearchScope
    @Binds
    abstract fun provideDynamicFilterModel(provider: ProductListPresenter): DynamicFilterModelProvider
}
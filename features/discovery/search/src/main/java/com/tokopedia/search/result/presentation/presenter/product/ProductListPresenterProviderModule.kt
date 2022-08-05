package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import dagger.Binds
import dagger.Module

@Module(includes = [ProductListPresenterModule::class])
abstract class ProductListPresenterProviderModule {

    @Binds
    @SearchScope
    abstract fun provideDynamicFilterModel(provider: ProductListPresenter): DynamicFilterModelProvider
}
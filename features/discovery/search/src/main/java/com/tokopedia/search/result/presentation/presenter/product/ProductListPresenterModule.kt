package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenter
import com.tokopedia.search.result.product.inspirationlistatc.InspirationListAtcPresenterDelegate
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenter
import com.tokopedia.search.result.product.lastfilter.LastFilterPresenterDelegate
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
    abstract fun provideLastFilterPresenter(provider: LastFilterPresenterDelegate): LastFilterPresenter
}

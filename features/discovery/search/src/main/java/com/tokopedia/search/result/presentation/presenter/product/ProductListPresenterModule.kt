package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.presentation.ProductListSectionContract
import dagger.Module
import dagger.Provides

@Module
class ProductListPresenterModule {

    @SearchScope
    @Provides
    fun provideProductListPresenter(presenter: ProductListPresenter): ProductListSectionContract.Presenter {
        return presenter
    }
}
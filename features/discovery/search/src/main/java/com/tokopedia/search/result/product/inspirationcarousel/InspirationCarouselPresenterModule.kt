package com.tokopedia.search.result.product.inspirationcarousel

import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class InspirationCarouselPresenterModule {

    @SearchScope
    @Provides
    fun provideProductListPresenter(presenter: InspirationCarouselPresenter): InspirationCarouselContract.Presenter {
        return presenter
    }
}
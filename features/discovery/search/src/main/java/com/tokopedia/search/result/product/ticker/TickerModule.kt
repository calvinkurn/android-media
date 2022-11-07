package com.tokopedia.search.result.product.ticker

import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object TickerModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideTickerPresenter(
        tickerPresenterDelegate: TickerPresenterDelegate
    ): TickerPresenter {
        return tickerPresenterDelegate
    }
}

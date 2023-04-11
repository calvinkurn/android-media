package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class BroadMatchModule {

    @Binds
    @SearchScope
    abstract fun provideBroadMatchView(
        broadMatchViewDelegate: BroadMatchViewDelegate
    ): BroadMatchView

    @Binds
    abstract fun provideBroadMatchPresenter(
        broadMatchPresenterDelegate: BroadMatchPresenterDelegate
    ): BroadMatchPresenter
}

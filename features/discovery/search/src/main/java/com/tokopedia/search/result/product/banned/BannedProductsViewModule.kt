package com.tokopedia.search.result.product.banned

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class BannedProductsViewModule {

    @SearchScope
    @Binds
    abstract fun provideBannedProductsView(
        bannedProductsViewDelegate: BannedProductsViewDelegate
    ): BannedProductsView
}

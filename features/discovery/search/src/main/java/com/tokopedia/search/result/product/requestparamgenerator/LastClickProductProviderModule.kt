package com.tokopedia.search.result.product.requestparamgenerator

import com.tokopedia.search.di.scope.SearchScope
import dagger.Binds
import dagger.Module

@Module
abstract class LastClickProductProviderModule {

    @SearchScope
    @Binds
    abstract fun provideLastClickedProductIdProvider(
        provider: LastClickedProductIdProviderImpl
    ): LastClickedProductIdProvider
}

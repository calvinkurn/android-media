package com.tokopedia.search.result.product.filter.dynamicfilter

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class DynamicFilterModelProviderModule {
    @Binds
    @SearchScope
    abstract fun bindMutableDynamicFilterModelProvider(
        providerDelegate: MutableDynamicFilterModelProviderDelegate,
    ): MutableDynamicFilterModelProvider

    @Binds
    @SearchScope
    abstract fun bindDynamicFilterModelProvider(
        mutableDynamicFilterModelProvider: MutableDynamicFilterModelProvider,
    ): DynamicFilterModelProvider
}

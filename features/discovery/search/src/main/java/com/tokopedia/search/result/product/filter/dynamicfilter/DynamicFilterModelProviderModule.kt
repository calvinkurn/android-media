package com.tokopedia.search.result.product.filter.dynamicfilter

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import dagger.Module
import dagger.Provides

@Module
object DynamicFilterModelProviderModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideMutableDynamicFilterModelProvider() : MutableDynamicFilterModelProvider {
        return MutableDynamicFilterModelProviderDelegate()
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideDynamicFilterModelProvider(
        mutableDynamicFilterModelProvider: MutableDynamicFilterModelProvider,
    ) : DynamicFilterModelProvider {
        return mutableDynamicFilterModelProvider
    }
}

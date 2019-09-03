package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class CartLocalCacheHandlerModule {

    @SearchScope
    @Provides
    fun provideCartLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, "CART")
    }
}
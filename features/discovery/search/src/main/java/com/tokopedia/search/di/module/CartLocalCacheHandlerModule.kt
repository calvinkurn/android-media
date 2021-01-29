package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.Cart.CART_LOCAL_CACHE_NAME
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class CartLocalCacheHandlerModule {

    @SearchScope
    @Provides
    @Named(SearchConstant.Cart.CART_LOCAL_CACHE)
    fun provideCartLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, CART_LOCAL_CACHE_NAME)
    }
}
package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.FREE_ONGKIR_LOCAL_CACHE_NAME
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class FreeOngkirLocalCacheModule {

    @SearchScope
    @Provides
    @Named(FREE_ONGKIR_LOCAL_CACHE_NAME)
    fun provideFreeOngkirLocalCache(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, FREE_ONGKIR_LOCAL_CACHE_NAME)
    }
}
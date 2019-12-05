package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.constants.SearchConstant.Advertising.ADVERTISING_LOCAL_CACHE
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class AdvertisingLocalCacheHandlerModule {

    @SearchScope
    @Provides
    @Named(ADVERTISING_LOCAL_CACHE)
    fun provideAdvertisingLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, SearchConstant.Advertising.ADVERTISING_ID)
    }
}
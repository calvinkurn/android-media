package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant.GCM_STORAGE
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class LocalCacheHandlerModule {

    @SearchScope
    @Provides
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, GCM_STORAGE)
    }
}
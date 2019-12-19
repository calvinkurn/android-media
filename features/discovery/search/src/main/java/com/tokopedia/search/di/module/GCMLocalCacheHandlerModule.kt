package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_LOCAL_CACHE
import com.tokopedia.discovery.common.constants.SearchConstant.GCM.GCM_STORAGE
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class GCMLocalCacheHandlerModule {

    @SearchScope
    @Provides
    @Named(GCM_LOCAL_CACHE)
    fun provideGTMLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, GCM_STORAGE)
    }
}
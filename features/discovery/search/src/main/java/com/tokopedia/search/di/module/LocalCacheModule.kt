package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.common.utils.MpsLocalCache
import com.tokopedia.discovery.common.utils.SharedPrefsMpsLocalCache
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
object LocalCacheModule {

    @Provides
    @SearchScope
    @JvmStatic
    fun provideMpsLocalCache(
        @ApplicationContext context: Context
    ) : MpsLocalCache {
        return SharedPrefsMpsLocalCache(context)
    }
}

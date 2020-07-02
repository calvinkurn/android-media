package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@SearchScope
@Module
class SearchOnBoardingLocalCacheModule {

    @SearchScope
    @Provides
    @Named(LOCAL_CACHE_NAME)
    fun provideSearchOnBoardingLocalCache(@SearchContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, LOCAL_CACHE_NAME)
    }
}
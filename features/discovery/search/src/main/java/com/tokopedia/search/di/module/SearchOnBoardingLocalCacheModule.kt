package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.discovery.common.constants.SearchConstant.OnBoarding.LOCAL_CACHE_NAME
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SearchOnBoardingLocalCacheModule {

    @SearchScope
    @Provides
    @Named(LOCAL_CACHE_NAME)
    fun provideSearchCoachMarkLocalCache(@SearchContext context: Context): CoachMarkLocalCache {
        return CoachMarkLocalCache(context)
    }
}
package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.common.utils.SharedPrefSimilarSearchCoachMarkLocalCache
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import dagger.Module
import dagger.Provides

@Module
object SimilarSearchLocalCacheModule {
    @JvmStatic
    @ProductCardOptionsScope
    @Provides
    fun provideSimilarSearchLocalCache(
        @ApplicationContext
        context: Context
    ) : SimilarSearchCoachMarkLocalCache {
        return SharedPrefSimilarSearchCoachMarkLocalCache(context)
    }
}

package com.tokopedia.productcard.options.di

import android.content.Context
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
        context: Context
    ) : SimilarSearchCoachMarkLocalCache {
        return SharedPrefSimilarSearchCoachMarkLocalCache(context)
    }
}

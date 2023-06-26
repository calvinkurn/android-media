package com.tokopedia.search.result.product.similarsearch

import android.content.Context
import com.tokopedia.discovery.common.utils.SharedPrefSimilarSearchCoachMarkLocalCache
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.onboarding.OnBoardingListenerDelegate
import dagger.Module
import dagger.Provides

@Module
object SimilarSearchModule {
    @JvmStatic
    @Provides
    @SearchScope
    fun provideSimilarSearchLocalCache(
        @SearchContext context: Context,
    ): SimilarSearchCoachMarkLocalCache {
        return SharedPrefSimilarSearchCoachMarkLocalCache(context)
    }

    @JvmStatic
    @Provides
    @SearchScope
    fun provideSimilarSearchView(
        similarSearchViewDelegate: OnBoardingListenerDelegate,
    ): SimilarSearchOnBoardingView {
        return similarSearchViewDelegate
    }
}

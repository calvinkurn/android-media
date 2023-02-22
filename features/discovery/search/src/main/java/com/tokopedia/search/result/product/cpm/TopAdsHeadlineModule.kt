package com.tokopedia.search.result.product.cpm

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper
import dagger.Module
import dagger.Provides

@Module
object TopAdsHeadlineModule {
    @SearchScope
    @Provides
    @JvmStatic
    fun provideTopAdsHeadlineHelper(): TopAdsHeadlineHelper {
        return TopAdsHeadlineHelper()
    }
}

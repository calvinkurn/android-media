package com.tokopedia.search.result.product.cpm

import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper
import dagger.Binds
import dagger.Module

@Module
abstract class TopAdsHeadlineModule {
    @SearchScope
    @Binds
    abstract fun provideTopAdsHeadlineHelper(helper: TopAdsHeadlineHelper) : TopAdsHeadlineHelper
}

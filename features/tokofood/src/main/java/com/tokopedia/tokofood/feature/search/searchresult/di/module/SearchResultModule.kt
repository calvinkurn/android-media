package com.tokopedia.tokofood.feature.search.searchresult.di.module

import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import dagger.Module
import dagger.Provides

@Module(includes = [SearchResultViewModelModule::class])
internal class SearchResultModule {

    @TokoFoodScope
    @Provides
    fun provideTokofoodFilterSortMapper(): TokofoodFilterSortMapper =
        TokofoodFilterSortMapper()

    @TokoFoodScope
    @Provides
    fun provideTokofoodMerchantSearchResultMapper(): TokofoodMerchantSearchResultMapper =
        TokofoodMerchantSearchResultMapper()

}
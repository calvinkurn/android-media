package com.tokopedia.tokofood.feature.search.searchresult.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.di.TokoFoodScope
import com.tokopedia.tokofood.feature.search.searchresult.di.scope.SearchResultScope
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodFilterSortMapper
import com.tokopedia.tokofood.feature.search.searchresult.domain.mapper.TokofoodMerchantSearchResultMapper
import dagger.Module
import dagger.Provides

@Module(includes = [SearchResultViewModelModule::class])
internal class SearchResultModule {

    @TokoFoodScope
    @Provides
    fun provideTokofoodFilterSortMapper(@ApplicationContext context: Context): TokofoodFilterSortMapper =
        TokofoodFilterSortMapper(context)

    @TokoFoodScope
    @Provides
    fun provideTokofoodMerchantSearchResultMapper(): TokofoodMerchantSearchResultMapper =
        TokofoodMerchantSearchResultMapper()

}
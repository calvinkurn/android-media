package com.tokopedia.tokofood.feature.search.searchresult.di.module

import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.tokofood.common.di.TokoFoodScope
import dagger.Module
import dagger.Provides

@Module
internal class SearchResultFilterModule {

    @TokoFoodScope
    @Provides
    fun provideFilterController(): FilterController = FilterController()

}
package com.tokopedia.search.di.module

import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class FilterControllerModule {

    @SearchScope
    @Provides
    fun provideFilterController(): FilterController = FilterController()
}
package com.tokopedia.autocompletecomponent.universal.di

import com.tokopedia.discovery.common.model.SearchParameter
import dagger.Module
import dagger.Provides

@Module
class UniversalSearchSearchParameterModule(
    private val searchParameter: SearchParameter
) {

    @UniversalSearchScope
    @Provides
    fun provideSearchParameter() : SearchParameter = searchParameter
}
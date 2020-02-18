package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponseMapper
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class PopularSearchMapperModule {

    @AutoCompleteScope
    @Provides
    internal fun providePopularSearchMapper(): PopularSearchResponseMapper {
        return PopularSearchResponseMapper()
    }
}
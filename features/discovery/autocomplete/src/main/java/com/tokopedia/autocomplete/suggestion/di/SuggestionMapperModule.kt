package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.SuggestionMapper
import dagger.Module
import dagger.Provides

@SuggestionScope
@Module
class SuggestionMapperModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionMapper(): SuggestionMapper {
        return SuggestionMapper()
    }
}
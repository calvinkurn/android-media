package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import com.tokopedia.autocomplete.suggestion.data.SuggestionApi
import com.tokopedia.autocomplete.suggestion.data.SuggestionDataSource
import com.tokopedia.autocomplete.suggestion.data.SuggestionRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class SuggestionRepositoryModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionRepository(
            @SuggestionQualifier suggestionApi: SuggestionApi
    ): SuggestionRepository {
        return SuggestionRepositoryImpl(
                SuggestionDataSource(suggestionApi)
        )
    }
}
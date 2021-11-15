package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionApi
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionDataSource
import com.tokopedia.autocompletecomponent.suggestion.data.SuggestionRepositoryImpl
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        SuggestionNetModule::class,
    ]
)
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
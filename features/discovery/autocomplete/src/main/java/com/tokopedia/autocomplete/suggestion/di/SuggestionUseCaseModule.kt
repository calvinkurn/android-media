package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import com.tokopedia.autocomplete.suggestion.SuggestionUseCase
import dagger.Module
import dagger.Provides

@SuggestionScope
@Module
class SuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionUseCase(
            suggestionRepository: SuggestionRepository
    ): SuggestionUseCase {
        return SuggestionUseCase(
                suggestionRepository
        )
    }
}
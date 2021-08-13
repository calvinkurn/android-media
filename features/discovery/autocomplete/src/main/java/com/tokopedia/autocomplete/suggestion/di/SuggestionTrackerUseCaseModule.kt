package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.SuggestionRepository
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionTrackerUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class SuggestionTrackerUseCaseModule {

    @SuggestionScope
    @Provides
    fun provideSuggestionUseCase(suggestionRepository: SuggestionRepository): UseCase<Void?> {
        return SuggestionTrackerUseCase(suggestionRepository)
    }
}
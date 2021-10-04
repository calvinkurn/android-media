package com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker

import com.tokopedia.autocompletecomponent.suggestion.SUGGESTION_TRACKER_USE_CASE
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionRepositoryModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(
    includes = [
        SuggestionRepositoryModule::class,
    ]
)
class SuggestionUrlTrackerUseCaseModule {

    @SuggestionScope
    @Provides
    @Named(SUGGESTION_TRACKER_USE_CASE)
    fun provideSuggestionUseCase(suggestionRepository: SuggestionRepository): UseCase<Void?> {
        return SuggestionTrackerUseCase(suggestionRepository)
    }
}
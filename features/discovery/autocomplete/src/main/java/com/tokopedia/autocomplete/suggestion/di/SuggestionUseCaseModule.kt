package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class SuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionUseCase(): UseCase<SuggestionUniverse> {
        return SuggestionUseCase(GraphqlUseCase())
    }
}
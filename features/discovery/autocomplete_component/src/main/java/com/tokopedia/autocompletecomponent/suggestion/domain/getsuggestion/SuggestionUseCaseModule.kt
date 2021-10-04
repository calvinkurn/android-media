package com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion

import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
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
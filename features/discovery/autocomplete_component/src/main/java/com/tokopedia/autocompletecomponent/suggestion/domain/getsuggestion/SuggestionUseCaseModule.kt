package com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion

import com.tokopedia.autocompletecomponent.suggestion.GET_SUGGESTION_USE_CASE
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    @Named(GET_SUGGESTION_USE_CASE)
    fun provideSuggestionUseCase(): UseCase<SuggestionUniverse> {
        return SuggestionUseCase(GraphqlUseCase())
    }
}
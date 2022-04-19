package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.autocompletecomponent.suggestion.GET_SUGGESTION_USE_CASE
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.usecase.coroutines.UseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class SuggestionUseCaseTestModule {

    @SuggestionScope
    @Provides
    @Named(GET_SUGGESTION_USE_CASE)
    fun getSuggestionUseCase(): UseCase<SuggestionUniverse> = SuggestionUseCaseTest()
}
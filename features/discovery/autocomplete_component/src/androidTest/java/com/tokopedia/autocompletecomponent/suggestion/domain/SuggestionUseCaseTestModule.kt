package com.tokopedia.autocompletecomponent.suggestion.domain

import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides

@Module
class SuggestionUseCaseTestModule {

//    @SuggestionScope
//    @Provides
//    fun provideUseCase(): UseCase<SuggestionUniverse> =
//        SuggestionUseCaseTestQuery(GraphqlUseCase())

    @Provides
    @SuggestionScope
    fun getSuggestionUseCase(): UseCase<SuggestionUniverse> = SuggestionUseCaseTest()
}
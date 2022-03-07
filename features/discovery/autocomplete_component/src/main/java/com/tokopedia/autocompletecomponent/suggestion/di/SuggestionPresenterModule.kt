package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.autocompletecomponent.suggestion.SuggestionContract
import com.tokopedia.autocompletecomponent.suggestion.SuggestionPresenter
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        SuggestionUserSessionInterfaceModule::class,
        SuggestionTopAdsUrlHitter::class,
    ]
)
class SuggestionPresenterModule {

    @SuggestionScope
    @Provides
    fun provideSuggestionPresenter(
        suggestionPresenter: SuggestionPresenter
    ): SuggestionContract.Presenter = suggestionPresenter
}
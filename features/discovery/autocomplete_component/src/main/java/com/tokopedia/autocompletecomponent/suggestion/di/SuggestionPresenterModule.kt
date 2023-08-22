package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.autocompletecomponent.di.LocalCacheModule
import com.tokopedia.autocompletecomponent.di.SchedulersProviderModule
import com.tokopedia.autocompletecomponent.suggestion.SuggestionContract
import com.tokopedia.autocompletecomponent.suggestion.SuggestionPresenter
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        SuggestionUserSessionInterfaceModule::class,
        SuggestionTopAdsUrlHitter::class,
        LocalCacheModule::class,
        SchedulersProviderModule::class,
        SuggestionContextModule::class,
    ]
)
class SuggestionPresenterModule {

    @SuggestionScope
    @Provides
    fun provideSuggestionPresenter(
        suggestionPresenter: SuggestionPresenter
    ): SuggestionContract.Presenter = suggestionPresenter
}

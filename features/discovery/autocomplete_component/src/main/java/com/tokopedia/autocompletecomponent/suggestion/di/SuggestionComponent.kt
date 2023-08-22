package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.di.AutoCompleteComponent
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTrackingModule
import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment
import com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion.SuggestionUseCaseModule
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionUrlTrackerUseCaseModule
import dagger.Component

@SuggestionScope
@Component(modules = [
    SuggestionUseCaseModule::class,
    SuggestionUrlTrackerUseCaseModule::class,
    SuggestionPresenterModule::class,
    SuggestionTrackingModule::class,
    SuggestionViewListenerModule::class,
], dependencies = [BaseAppComponent::class])
interface SuggestionComponent {

    fun inject(fragment: SuggestionFragment)
}

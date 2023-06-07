package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.di.AutoCompleteComponent
import com.tokopedia.autocompletecomponent.suggestion.analytics.SuggestionTrackingTestModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionPresenterModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionUseCaseTestModule
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionUrlTrackerUseCaseModule
import dagger.Component

@SuggestionScope
@Component(modules = [
    SuggestionUseCaseTestModule::class,
    SuggestionUrlTrackerUseCaseModule::class,
    SuggestionTrackingTestModule::class,
    SuggestionPresenterModule::class,
    SuggestionViewListenerModule::class
], dependencies = [BaseAppComponent::class])
interface SuggestionTestComponent: SuggestionComponent

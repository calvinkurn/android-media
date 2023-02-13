package com.tokopedia.content.common.producttag.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionPresenterModule
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionScope
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule
import com.tokopedia.autocompletecomponent.suggestion.domain.getsuggestion.SuggestionUseCaseModule
import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionUrlTrackerUseCaseModule
import com.tokopedia.content.common.producttag.di.module.FeedSuggestionTrackingModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
@SuggestionScope
@Component(
    modules = [
        SuggestionUseCaseModule::class,
        SuggestionUrlTrackerUseCaseModule::class,
        SuggestionPresenterModule::class,
        FeedSuggestionTrackingModule::class,
        SuggestionViewListenerModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface FeedSuggestionComponent : SuggestionComponent
package com.tokopedia.createpost.producttag.view.activity

import com.tokopedia.autocompletecomponent.BaseAutoCompleteActivity
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.createpost.producttag.di.DaggerFeedInitialStateComponent
import com.tokopedia.createpost.producttag.di.DaggerFeedSuggestionComponent

/**
 * Created By : Jonathan Darwin on May 24, 2022
 */
class FeedAutoCompleteActivity : BaseAutoCompleteActivity() {

    override fun createInitialStateComponent(): InitialStateComponent {
        return DaggerFeedInitialStateComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .initialStateViewListenerModule(getInitialStateViewListenerModule())
            .build()
    }

    override fun createSuggestionComponent(): SuggestionComponent {
        return DaggerFeedSuggestionComponent.builder()
            .baseAppComponent(getBaseAppComponent())
            .suggestionViewListenerModule(getSuggestionViewListenerModule())
            .build()
    }
}
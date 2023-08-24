package com.tokopedia.autocompletecomponent

import com.tokopedia.autocompletecomponent.initialstate.DaggerInitialStateTestComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateContextModule
import com.tokopedia.autocompletecomponent.suggestion.DaggerSuggestionTestComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionContextModule

class AutoCompleteActivitySample: BaseAutoCompleteActivity() {

    override fun createInitialStateComponent(): InitialStateComponent =
        DaggerInitialStateTestComponent
            .builder()
            .initialStateContextModule(InitialStateContextModule(this))
            .baseAppComponent(getBaseAppComponent())
            .initialStateViewListenerModule(getInitialStateViewListenerModule())
            .build()

    override fun createSuggestionComponent(): SuggestionComponent =
        DaggerSuggestionTestComponent
            .builder()
            .suggestionContextModule(SuggestionContextModule(this))
            .baseAppComponent(getBaseAppComponent())
            .suggestionViewListenerModule(getSuggestionViewListenerModule())
            .build()

    override fun getBaseAppComponent() = createFakeBaseAppComponent(this)
}

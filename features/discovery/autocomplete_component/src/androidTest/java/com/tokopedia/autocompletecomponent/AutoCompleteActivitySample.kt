package com.tokopedia.autocompletecomponent

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.initialstate.DaggerInitialStateTestComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateComponent
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateViewListenerModule
import com.tokopedia.autocompletecomponent.suggestion.DaggerSuggestionTestComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionComponent
import com.tokopedia.autocompletecomponent.suggestion.di.SuggestionViewListenerModule

class AutoCompleteActivitySample: BaseAutoCompleteActivity() {

    override fun createInitialStateComponent(baseAppComponent: BaseAppComponent): InitialStateComponent =
        DaggerInitialStateTestComponent
            .builder()
            .baseAppComponent(getBaseAppComponent())
            .initialStateViewListenerModule(InitialStateViewListenerModule(this))
            .build()

    override fun createSuggestionComponent(baseAppComponent: BaseAppComponent): SuggestionComponent =
        DaggerSuggestionTestComponent
            .builder()
            .baseAppComponent(getBaseAppComponent())
            .suggestionViewListenerModule(SuggestionViewListenerModule(this))
            .build()

    override fun getBaseAppComponent() = createFakeBaseAppComponent(this)
}
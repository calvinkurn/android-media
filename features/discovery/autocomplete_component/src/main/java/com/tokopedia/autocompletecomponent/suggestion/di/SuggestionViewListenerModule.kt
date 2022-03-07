package com.tokopedia.autocompletecomponent.suggestion.di

import com.tokopedia.autocompletecomponent.suggestion.SuggestionFragment.SuggestionViewUpdateListener
import dagger.Module
import dagger.Provides

@Module
class SuggestionViewListenerModule(
    private val listener: SuggestionViewUpdateListener,
) {

    @SuggestionScope
    @Provides
    fun provide(): SuggestionViewUpdateListener = listener
}
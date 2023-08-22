package com.tokopedia.autocompletecomponent.suggestion.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SuggestionContextModule(private val context: Context) {

    @SuggestionScope
    @Provides
    fun provideSearchContext(): Context = context
}

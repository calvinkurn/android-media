package com.tokopedia.autocomplete.suggestion.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SuggestionContextModule(private val context: Context) {

    @SuggestionContext
    @Provides
    fun provideSuggestionContext(): Context = context
}
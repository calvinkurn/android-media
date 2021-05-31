package com.tokopedia.tokomart.search.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SearchContextModule(private val context: Context) {

    @SearchScope
    @Provides
    fun provideContext(): Context = context
}
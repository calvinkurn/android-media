package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class SearchContextModule(private val context: Context) {

    @SearchContext
    @Provides
    fun provideSearchContext(): Context = context
}
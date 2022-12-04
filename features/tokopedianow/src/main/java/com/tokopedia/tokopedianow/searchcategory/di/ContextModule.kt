package com.tokopedia.tokopedianow.searchcategory.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {
    @SearchCategoryScope
    @Provides
    fun provideContext() : Context = context
}

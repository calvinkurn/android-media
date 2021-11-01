package com.tokopedia.tokopedianow.category.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class CategoryContextModule(private val context: Context) {

    @CategoryScope
    @Provides
    fun provideContext(): Context = context
}
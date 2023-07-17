package com.tokopedia.tokopedianow.category.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class CategoryContextModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context
}

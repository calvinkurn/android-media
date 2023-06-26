package com.tokopedia.tokopedianow.category.di.module

import android.content.Context
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import dagger.Module
import dagger.Provides

@Module
class CategoryContextModule(
    private val context: Context
) {
    @CategoryScope
    @Provides
    fun provideContext(): Context = context
}

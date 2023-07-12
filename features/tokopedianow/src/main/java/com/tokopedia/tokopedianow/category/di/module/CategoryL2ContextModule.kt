package com.tokopedia.tokopedianow.category.di.module

import android.content.Context
import com.tokopedia.tokopedianow.category.di.scope.CategoryL2Scope
import dagger.Module
import dagger.Provides

@Module
class CategoryL2ContextModule(
    private val context: Context
) {
    @CategoryL2Scope
    @Provides
    fun provideContext(): Context = context
}

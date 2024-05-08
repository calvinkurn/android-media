package com.tokopedia.tokopedianow.category.di.module

import android.content.Context
import com.tokopedia.tokopedianow.category.di.scope.CategoryScope
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.helper.ResourceProviderImpl
import dagger.Module
import dagger.Provides

@Module
class CategoryResourceModule {
    @CategoryScope
    @Provides
    fun provideResourceProvider(context: Context): ResourceProvider = ResourceProviderImpl(context)
}

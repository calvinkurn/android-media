package com.tokopedia.search.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class ResourcesModule {

    @SearchScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}
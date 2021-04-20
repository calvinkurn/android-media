package com.tokopedia.search.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class ResourcesModule {

    @SearchScope
    @Provides
    fun provideResources(@SearchContext context: Context): Resources {
        return context.resources
    }
}
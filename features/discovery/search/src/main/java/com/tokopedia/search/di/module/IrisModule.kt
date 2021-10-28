package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class IrisModule {

    @SearchScope
    @Provides
    fun provideIrisAnalytics(@SearchContext context: Context): Iris =
        IrisAnalytics.getInstance(context)
}
package com.tokopedia.search.result.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
@SearchScope
class EmptySearchCreatorModule {

    @Provides
    @SearchScope
    fun provideEmptySearchCreator(@ApplicationContext context: Context): EmptySearchCreator {
        return EmptySearchCreator(context)
    }
}

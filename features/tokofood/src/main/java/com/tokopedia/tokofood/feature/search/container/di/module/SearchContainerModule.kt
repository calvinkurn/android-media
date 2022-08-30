package com.tokopedia.tokofood.feature.search.container.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokofood.feature.search.container.di.scope.SearchContainerScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [SearchContainerViewModelModule::class])
internal class SearchContainerModule {

    @SearchContainerScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)
}
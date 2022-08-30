package com.tokopedia.tokofood.feature.search.initialstate.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.tokofood.feature.search.container.di.scope.SearchContainerScope
import com.tokopedia.tokofood.feature.search.initialstate.di.scope.InitialStateScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [InitialStateViewModelModule::class])
internal class InitialStateSearchModule {

    @InitialStateScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)
}
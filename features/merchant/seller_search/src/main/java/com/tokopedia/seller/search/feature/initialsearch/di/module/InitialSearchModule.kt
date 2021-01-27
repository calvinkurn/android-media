package com.tokopedia.seller.search.feature.initialsearch.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.seller.search.feature.initialsearch.di.scope.InitialSearchScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [InitialSearchViewModelModule::class])
class InitialSearchModule {

    @InitialSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @InitialSearchScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }
}
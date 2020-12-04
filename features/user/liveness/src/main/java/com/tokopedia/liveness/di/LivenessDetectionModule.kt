package com.tokopedia.liveness.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.liveness.utils.CoroutineDispatchers
import com.tokopedia.liveness.utils.CoroutineDispatchersProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class LivenessDetectionModule {

    @LivenessDetectionScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @LivenessDetectionScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}
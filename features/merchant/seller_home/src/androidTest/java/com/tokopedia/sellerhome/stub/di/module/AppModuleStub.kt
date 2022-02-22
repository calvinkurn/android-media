package com.tokopedia.sellerhome.stub.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhome.stub.common.CoroutineAndroidTestDispatchersProvider
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 25/11/21.
 */

@Module
class AppModuleStub(private val context: Context) {

    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
    }

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineAndroidTestDispatchersProvider
    }
}
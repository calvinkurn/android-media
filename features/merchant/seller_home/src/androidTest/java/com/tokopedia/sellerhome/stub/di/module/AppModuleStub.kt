package com.tokopedia.sellerhome.stub.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhome.stub.common.CoroutineAndroidTestDispatchersProvider
import com.tokopedia.sellerhomecommon.common.di.annotation.ActivityContext
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
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @ApplicationScope
    @Provides
    @ActivityContext
    fun provideActivityContext(): Context {
        return context
    }

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineAndroidTestDispatchersProvider
    }
}
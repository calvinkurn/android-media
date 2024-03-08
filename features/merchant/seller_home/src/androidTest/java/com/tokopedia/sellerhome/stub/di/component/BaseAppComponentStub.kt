package com.tokopedia.sellerhome.stub.di.component

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerhome.stub.di.module.AppModuleStub
import com.tokopedia.sellerhomecommon.common.di.annotation.ActivityContext
import dagger.Component

/**
 * Created by @ilhamsuaib on 25/11/21.
 */

@ApplicationScope
@Component(modules = [AppModuleStub::class])
interface BaseAppComponentStub {

    @ApplicationContext
    fun getApplicationContext(): Context

    @ActivityContext
    fun getActivityContext(): Context

    fun coroutineDispatchers(): CoroutineDispatchers
}
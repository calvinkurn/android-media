package com.tokopedia.home_account.stub.di

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.stub.di.user.*

class ActivityComponentFactoryStub : ActivityComponentFactory() {

    val component: HomeAccountUserComponentsStub

    init {
        val base = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(InstrumentationRegistry.getInstrumentation().context)).build()
        component = DaggerHomeAccountUserComponentsStub.builder()
            .fakeBaseAppComponent(base)
            .fakeHomeAccountUserModules(FakeHomeAccountUserModules(InstrumentationRegistry.getInstrumentation().targetContext)).build()
    }

    override fun createHomeAccountComponent(
        context: Context,
        application: Application
    ): HomeAccountUserComponents {
        return component
    }
}

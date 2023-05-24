package com.tokopedia.home_account.stub.di

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.di.HomeAccountUserComponents
import com.tokopedia.home_account.explicitprofile.di.component.DaggerExplicitProfileComponents
import com.tokopedia.home_account.explicitprofile.di.component.ExplicitProfileComponents
import com.tokopedia.home_account.stub.di.user.*

class ActivityComponentFactoryStub : ActivityComponentFactory() {

    val component: HomeAccountUserComponentsStub
    val base: FakeBaseAppComponent = DaggerFakeBaseAppComponent.builder()
        .fakeAppModule(FakeAppModule(InstrumentationRegistry.getInstrumentation().context)).build()

    init {
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

    override fun createExplicitProfileComponent(application: Application): ExplicitProfileComponents {
        return DaggerExplicitProfileComponents.builder().baseAppComponent(base).build()
    }
}

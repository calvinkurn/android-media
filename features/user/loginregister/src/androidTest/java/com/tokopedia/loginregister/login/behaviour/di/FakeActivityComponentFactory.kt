package com.tokopedia.loginregister.login.behaviour.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.loginregister.login.behaviour.di.modules.AppModuleStub
import com.tokopedia.loginregister.login.behaviour.di.modules.FakeLoginModule
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.di.LoginModule

class FakeActivityComponentFactory: ActivityComponentFactory() {

    val component: LoginComponentStub

    init {
        val baseComponent = DaggerTestAppComponent.builder()
            .appModuleStub(AppModuleStub(ApplicationProvider.getApplicationContext()))
            .build()
        component = DaggerLoginComponentStub.builder()
            .loginModule(FakeLoginModule)
            .baseAppComponent(baseComponent)
            .build()
    }

    override fun createActivityComponent(application: Application): LoginComponent {
        return component
    }

}
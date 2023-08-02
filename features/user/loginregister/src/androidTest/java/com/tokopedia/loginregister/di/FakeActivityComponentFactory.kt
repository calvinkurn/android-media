package com.tokopedia.loginregister.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginregister.common.utils.BasicIdlingResource
import com.tokopedia.loginregister.di.modules.AppModuleStub
import com.tokopedia.loginregister.di.modules.FakeLoginModule
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.redefineregisteremail.di.DaggerRedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailModule
import com.tokopedia.loginregister.redefineregisteremail.stub.TestIdlingResourceProvider
import com.tokopedia.loginregister.registerinitial.di.RegisterInitialComponent

class FakeActivityComponentFactory : ActivityComponentFactory() {

    val loginComponent: LoginComponentStub
    val registerComponent: RegisterInitialComponentStub

    init {
        val baseComponent = DaggerTestAppComponent.builder()
            .appModuleStub(AppModuleStub(ApplicationProvider.getApplicationContext()))
            .build()
        loginComponent = DaggerLoginComponentStub.builder()
            .loginModule(FakeLoginModule)
            .baseAppComponent(baseComponent)
            .build()

        registerComponent = DaggerRegisterInitialComponentStub.builder()
            .baseAppComponent(baseComponent)
            .build()
    }

    override fun createLoginComponent(application: Application): LoginComponent {
        return loginComponent
    }

    override fun createRegisterComponent(application: Application): RegisterInitialComponent {
        return registerComponent
    }

    override fun createRedefineRegisterEmailComponent(application: Application): RedefineRegisterEmailComponent {
        return DaggerRedefineRegisterEmailComponent.builder()
            .baseAppComponent((ApplicationProvider.getApplicationContext() as BaseMainApplication).baseAppComponent)
            .redefineRegisterEmailModule(object : RedefineRegisterEmailModule() {
                override fun provideBasicIdlingResource(): BasicIdlingResource {
                    return TestIdlingResourceProvider.instance()
                }
            }).build()
    }
}

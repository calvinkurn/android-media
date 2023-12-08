package com.tokopedia.tokopedianow.home.di.component

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.tokopedianow.test.di.module.AppModuleStub
import com.tokopedia.tokopedianow.home.di.factory.HomeComponentFactory
import com.tokopedia.tokopedianow.home.di.module.HomeModuleStub
import com.tokopedia.tokopedianow.test.di.component.DaggerBaseAppComponentStub

class HomeComponentStubFactory : HomeComponentFactory() {

    val homeComponent: HomeComponentStub
    val homeModuleStub: HomeModuleStub
    val appModuleStub: AppModuleStub

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext

        appModuleStub = AppModuleStub(context)
        homeModuleStub = HomeModuleStub()

        val baseAppComponentStub = DaggerBaseAppComponentStub.builder()
            .appModuleStub(appModuleStub)
            .build()

        homeComponent = DaggerHomeComponentStub.builder()
            .baseAppComponentStub(baseAppComponentStub)
            .homeModuleStub(homeModuleStub)
            .build()
    }

    override fun createComponent(application: Application): HomeComponent {
        return homeComponent
    }
}

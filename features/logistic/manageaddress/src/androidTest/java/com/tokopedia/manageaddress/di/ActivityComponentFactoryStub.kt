package com.tokopedia.manageaddress.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.manageaddress.di.module.ManageAddressModule
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey.KEY_SHARE_ADDRESS_LOGI
import io.mockk.every
import io.mockk.mockk

class ActivityComponentFactoryStub : ActivityComponentFactory() {

    val appcomponent: TestAppComponent
    val activityComponent: ManageAddressComponentStub

    init {
        appcomponent = DaggerTestAppComponent.builder()
            .fakeAppModule(FakeAppModule(ApplicationProvider.getApplicationContext())).build()
        activityComponent = DaggerManageAddressComponentStub.builder()
            .manageAddressModule(object : ManageAddressModule() {
                override fun provideRemoteConfigAbTest(): RemoteConfig {
                    return mockk() {
                        every { getString(KEY_SHARE_ADDRESS_LOGI, "") } returns KEY_SHARE_ADDRESS_LOGI
                    }
                }
            })
            .testAppComponent(appcomponent).build()
    }

    override fun createComponent(application: Application): ManageAddressComponent {
        return activityComponent
    }
}

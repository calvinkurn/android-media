package com.tokopedia.manageaddress.di

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.manageaddress.di.module.ManageAddressModule

class ActivityComponentFactoryStub : ActivityComponentFactory() {

    val appcomponent: TestAppComponent
    val activityComponent: ManageAddressComponentStub

    init {
        appcomponent = DaggerTestAppComponent.builder()
            .fakeAppModule(FakeAppModule(ApplicationProvider.getApplicationContext())).build()
        activityComponent = DaggerManageAddressComponentStub.builder()
            .manageAddressModule(ManageAddressModule())
            .testAppComponent(appcomponent).build()
    }

    override fun createComponent(application: Application): ManageAddressComponent {
        return activityComponent
    }
}

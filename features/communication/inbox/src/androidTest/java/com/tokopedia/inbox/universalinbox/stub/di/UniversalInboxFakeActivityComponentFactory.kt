package com.tokopedia.inbox.universalinbox.stub.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.inbox.universalinbox.di.UniversalInboxActivityComponentFactory
import com.tokopedia.inbox.universalinbox.di.UniversalInboxComponent
import com.tokopedia.inbox.universalinbox.stub.di.base.DaggerUniversalInboxFakeBaseAppComponent
import com.tokopedia.inbox.universalinbox.stub.di.base.UniversalInboxFakeAppModule
import com.tokopedia.inbox.universalinbox.stub.di.base.UniversalInboxFakeBaseAppComponent

class UniversalInboxFakeActivityComponentFactory : UniversalInboxActivityComponentFactory() {

    val universalInboxComponent: UniversalInboxComponentStub
    private val baseComponent: UniversalInboxFakeBaseAppComponent

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        baseComponent = DaggerUniversalInboxFakeBaseAppComponent.builder()
            .universalInboxFakeAppModule(UniversalInboxFakeAppModule(context))
            .build()
        universalInboxComponent = DaggerUniversalInboxComponentStub.builder()
            .universalInboxFakeBaseAppComponent(baseComponent)
            .build()
    }

    override fun createUniversalInboxComponent(application: Application): UniversalInboxComponent {
        return universalInboxComponent
    }
}

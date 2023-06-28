package com.tokopedia.notifcenter.stub.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.stub.di.base.DaggerNotificationFakeBaseAppComponent
import com.tokopedia.notifcenter.stub.di.base.NotificationFakeAppModule
import com.tokopedia.notifcenter.stub.di.base.NotificationFakeBaseAppComponent

class NotificationFakeActivityComponentFactory : NotificationActivityComponentFactory() {

    val notificationComponent: NotificationComponentStub
    private val baseComponent: NotificationFakeBaseAppComponent

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        baseComponent = DaggerNotificationFakeBaseAppComponent.builder()
            .notificationFakeAppModule(NotificationFakeAppModule(context))
            .build()
        notificationComponent = DaggerNotificationComponentStub.builder()
            .notificationFakeBaseAppComponent(baseComponent)
            .build()
    }

    override fun createNotificationComponent(application: Application): NotificationComponent {
        return notificationComponent
    }
}

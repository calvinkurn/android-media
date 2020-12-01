package com.tokopedia.notifications.di

import com.tokopedia.notifications.di.module.GraphQueryModule
import com.tokopedia.notifications.di.module.LifecycleModule
import com.tokopedia.notifications.di.module.NotificationModule
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.notifications.inApp.viewEngine.CMActivityLifeCycle
import com.tokopedia.notifications.receiver.CMBroadcastReceiver
import dagger.Component

@CMNotificationScope
@Component(modules = [
    GraphQueryModule::class,
    NotificationModule::class,
    LifecycleModule::class
])
interface CMNotificationComponent {
    fun inject(broadcast: CMBroadcastReceiver)
    fun inject(lifecycle: CMActivityLifeCycle)
}
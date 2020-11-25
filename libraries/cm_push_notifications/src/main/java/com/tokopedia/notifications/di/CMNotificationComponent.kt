package com.tokopedia.notifications.di

import com.tokopedia.notifications.di.module.GraphQueryModule
import com.tokopedia.notifications.di.module.NotificationModule
import com.tokopedia.notifications.di.scope.CMNotificationScope
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.receiver.CMBroadcastReceiver
import dagger.Component

@CMNotificationScope
@Component(modules = [
    GraphQueryModule::class,
    NotificationModule::class
])
interface CMNotificationComponent {
    fun inject(broadcast: CMBroadcastReceiver)
    fun inject(manager: CMInAppManager)
}
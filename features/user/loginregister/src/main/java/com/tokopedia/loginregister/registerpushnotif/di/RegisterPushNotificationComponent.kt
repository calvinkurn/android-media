package com.tokopedia.loginregister.registerpushnotif.di

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import dagger.Component

@ActivityScope
@Component(modules = [RegisterPushNotificationModule::class])
interface RegisterPushNotificationComponent {
    fun inject(registerPushNotificationWorker: RegisterPushNotificationWorker)
}
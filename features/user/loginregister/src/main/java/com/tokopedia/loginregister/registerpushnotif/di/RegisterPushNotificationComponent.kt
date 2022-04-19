package com.tokopedia.loginregister.registerpushnotif.di

import com.tokopedia.loginregister.common.di.LoginRegisterScope
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import dagger.Component

@LoginRegisterScope
@Component(modules = [RegisterPushNotificationModule::class])
interface RegisterPushNotificationComponent {
    fun inject(registerPushNotificationWorker: RegisterPushNotificationWorker)
}
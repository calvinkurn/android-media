package com.tokopedia.fcmcommon.di

import com.tokopedia.fcmcommon.FirebaseMessagingManager
import com.tokopedia.fcmcommon.service.SyncFcmTokenService
import dagger.Component

@FcmScope
@Component(modules = [FcmModule::class])
interface FcmComponent {
    fun firebaseMessagingManager(): FirebaseMessagingManager
    fun inject(syncFcmTokenService: SyncFcmTokenService)
}

package com.tokopedia.fcmcommon.di

import com.tokopedia.fcmcommon.FirebaseMessagingManager
import dagger.Component

@FcmScope
@Component(modules = [FcmModule::class, FcmQueryModule::class])
interface FcmComponent {
    fun firebaseMessagingManager(): FirebaseMessagingManager
}

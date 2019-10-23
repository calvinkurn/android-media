package com.tokopedia.fcmcommon.di

import com.google.firebase.messaging.FirebaseMessagingService
import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [FcmModule::class])
interface FcmComponent {
    fun inject(messagingService: FirebaseMessagingService)
}

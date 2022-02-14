package com.tokopedia.tokopoints.notification.di

import com.tokopedia.tokopoints.notification.view.TokopointNotifActivity
import dagger.Component

@Component(modules = [TokopointNotifModule::class])
interface TokopointNotifComponent {
    fun inject(tokopointNotifActivity: TokopointNotifActivity)
}
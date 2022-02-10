package com.tokopedia.tokopoints.notification.di

import com.tokopedia.tokopoints.notification.view.TokomemberActivity
import dagger.Component
import javax.inject.Scope

@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class TokoPointNotifScope

@Component(modules = [TokopointNotifModule::class])
interface TokopointNotifComponent {
    fun inject(tokomemberActivity: TokomemberActivity)
}
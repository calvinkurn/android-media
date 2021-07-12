package com.tokopedia.troubleshooter.notification.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.fcmcommon.di.FcmComponent
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootModule
import com.tokopedia.troubleshooter.notification.di.module.TroubleshootViewModelModule
import com.tokopedia.troubleshooter.notification.ui.fragment.TroubleshootFragment
import dagger.Component

@TroubleshootScope
@Component(modules = [
    TroubleshootModule::class,
    TroubleshootViewModelModule::class], dependencies = [
    FcmComponent::class, BaseAppComponent::class
])
interface TroubleshootComponent {
    fun inject(fragment: TroubleshootFragment)
}
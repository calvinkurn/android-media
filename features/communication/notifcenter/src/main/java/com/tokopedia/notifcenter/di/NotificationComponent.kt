package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.di.module.NotificationModule
import com.tokopedia.notifcenter.di.module.NotificationQueryModule
import com.tokopedia.notifcenter.di.module.NotificationViewModelModule
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.presentation.activity.NotificationAffiliateActivity
import com.tokopedia.notifcenter.presentation.fragment.NotificationAffiliateFragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment
import com.tokopedia.notifcenter.service.MarkAsSeenService
import dagger.Component

@NotificationScope
@Component(
    modules = [
        CommonModule::class,
        NotificationQueryModule::class,
        NotificationViewModelModule::class,
        NotificationModule::class
    ],
    dependencies = [(BaseAppComponent::class)]
)
interface NotificationComponent {
    fun inject(fragment: NotificationFragment)
    fun inject(fragment: NotificationAffiliateFragment)
    fun inject(activity: NotificationAffiliateActivity)
    fun inject(service: MarkAsSeenService)
}

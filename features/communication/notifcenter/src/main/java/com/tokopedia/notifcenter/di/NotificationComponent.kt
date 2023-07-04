package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.di.module.NotificationModule
import com.tokopedia.notifcenter.di.module.NotificationViewModelModule
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.service.MarkAsSeenService
import com.tokopedia.notifcenter.ui.NotificationFragment
import com.tokopedia.notifcenter.ui.affiliate.NotificationAffiliateActivity
import com.tokopedia.notifcenter.ui.affiliate.NotificationAffiliateFragment
import com.tokopedia.notifcenter.ui.buyer.NotificationActivity
import com.tokopedia.notifcenter.ui.buyer.bottomsheet.NotifCenterAccountSwitcherBottomSheet
import dagger.Component

@NotificationScope
@Component(
    modules = [
        CommonModule::class,
        NotificationViewModelModule::class,
        NotificationModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface NotificationComponent {
    fun inject(fragment: NotificationFragment)
    fun inject(fragment: NotificationAffiliateFragment)
    fun inject(activity: NotificationAffiliateActivity)
    fun inject(service: MarkAsSeenService)
    fun inject(activity: NotificationActivity)
    fun inject(fragment: NotifCenterAccountSwitcherBottomSheet)
}

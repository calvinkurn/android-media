package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.di.module.NotificationCommonModule
import com.tokopedia.notifcenter.di.module.NotificationFragmentModule
import com.tokopedia.notifcenter.di.module.NotificationModule
import com.tokopedia.notifcenter.di.module.NotificationViewModelModule
import com.tokopedia.notifcenter.service.MarkAsSeenService
import com.tokopedia.notifcenter.ui.affiliate.NotificationAffiliateActivity
import com.tokopedia.notifcenter.ui.buyer.NotificationActivity
import com.tokopedia.notifcenter.ui.buyer.bottomsheet.NotifCenterAccountSwitcherBottomSheet
import com.tokopedia.notifcenter.ui.seller.NotificationSellerActivity
import dagger.Component

@ActivityScope
@Component(
    modules = [
        NotificationCommonModule::class,
        NotificationViewModelModule::class,
        NotificationModule::class,
        NotificationFragmentModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface NotificationComponent {
    fun inject(activity: NotificationAffiliateActivity)
    fun inject(service: MarkAsSeenService)
    fun inject(activity: NotificationActivity)
    fun inject(activity: NotificationSellerActivity)
    fun inject(fragment: NotifCenterAccountSwitcherBottomSheet)
}

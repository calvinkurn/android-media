package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.di.module.*
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.presentation.BaseNotificationFragment
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.notifcenter.presentation.fragment.ProductStockHandlerDialog
import dagger.Component

@NotificationScope
@Component(
        modules = [
            CommonModule::class,
            NotificationQueryModule::class,
            NotificationUpdateModule::class,
            NotificationTransactionModule::class,
            NotificationViewModelModule::class
        ],
        dependencies = [(BaseAppComponent::class)]
)
interface NotificationComponent {
    fun inject(fragment: NotificationTransactionFragment)
    fun inject(fragment: NotificationUpdateFragment)
    fun inject(fragment: BaseNotificationFragment)
    fun inject(dialog: ProductStockHandlerDialog)
    fun inject(activity: NotificationActivity)
}
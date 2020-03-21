package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.di.module.*
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.notifcenter.presentation.activity.NotificationActivity
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.presentation.fragment.NotificationUpdateFragment
import com.tokopedia.notifcenter.presentation.fragment.ProductStockReminderDialog
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
    //activity
    fun inject(activity: NotificationActivity)

    //fragment
    fun inject(fragment: NotificationTransactionFragment)
    fun inject(fragment: NotificationUpdateFragment)

    //dialog
    fun inject(dialog: ProductStockReminderDialog)
}
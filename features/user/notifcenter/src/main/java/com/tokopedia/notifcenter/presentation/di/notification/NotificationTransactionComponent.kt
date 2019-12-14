package com.tokopedia.notifcenter.presentation.di.notification

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.presentation.di.notification.module.CommonModule
import com.tokopedia.notifcenter.presentation.di.notification.module.NotificationQueryModule
import com.tokopedia.notifcenter.presentation.di.notification.module.NotificationTransactionModule
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.presentation.di.notification.module.NotificationTransactionViewModelModule
import com.tokopedia.notifcenter.presentation.di.notification.scope.NotificationTransactionScope
import dagger.Component

@NotificationTransactionScope
@Component(
        modules = [
            CommonModule::class,
            NotificationQueryModule::class,
            NotificationTransactionModule::class,
            NotificationTransactionViewModelModule::class
        ],
        dependencies = [(BaseAppComponent::class)]
)
interface NotificationTransactionComponent {
    fun inject(fragment: NotificationTransactionFragment)
}
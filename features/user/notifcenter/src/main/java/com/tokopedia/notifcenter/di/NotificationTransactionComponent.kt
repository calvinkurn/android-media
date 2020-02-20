package com.tokopedia.notifcenter.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.notifcenter.di.module.CommonModule
import com.tokopedia.notifcenter.di.module.NotificationQueryModule
import com.tokopedia.notifcenter.di.module.NotificationTransactionModule
import com.tokopedia.notifcenter.presentation.fragment.NotificationTransactionFragment
import com.tokopedia.notifcenter.di.module.NotificationTransactionViewModelModule
import com.tokopedia.notifcenter.di.scope.NotificationTransactionScope
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
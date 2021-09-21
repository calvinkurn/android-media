package com.tokopedia.inbox.fake.di.notifcenter

import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.NotificationQueryModule
import com.tokopedia.notifcenter.di.module.NotificationTransactionModule
import com.tokopedia.notifcenter.di.module.NotificationUpdateModule
import com.tokopedia.notifcenter.di.module.NotificationViewModelModule
import com.tokopedia.notifcenter.di.scope.NotificationScope
import dagger.Component

@NotificationScope
@Component(
    modules = [
        FakeCommonModule::class,
        NotificationQueryModule::class,
        NotificationUpdateModule::class,
        NotificationTransactionModule::class,
        NotificationViewModelModule::class,
        FakeNotificationModule::class,
        FakeNotifcenterUsecase::class
    ],
    dependencies = [(FakeBaseAppComponent::class)]
)
interface FakeNotificationComponent : NotificationComponent {
    fun injectMembers(inboxNotifcenterDep: InboxNotifcenterFakeDependency)
}
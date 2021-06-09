package com.tokopedia.inbox.fake.di.notifcenter

import com.tokopedia.inbox.fake.InboxNotifcenterFakeDependency
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.di.module.*
import com.tokopedia.notifcenter.di.scope.NotificationScope
import dagger.Component

@NotificationScope
@Component(
    modules = [
        CommonModule::class,
        NotificationQueryModule::class,
        NotificationUpdateModule::class,
        NotificationTransactionModule::class,
        NotificationViewModelModule::class,
        NotificationModule::class
    ],
    dependencies = [(FakeBaseAppComponent::class)]
)
interface FakeNotificationComponent : NotificationComponent {
    fun injectMembers(inboxNotifcenterDep: InboxNotifcenterFakeDependency)
}
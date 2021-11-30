package com.tokopedia.inbox.fake.di

import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.di.InboxScope
import com.tokopedia.inbox.di.InboxViewModelModule
import com.tokopedia.inbox.fake.InboxFakeDependency
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import dagger.Component

@InboxScope
@Component(
    modules = [
        InboxViewModelModule::class,
        FakeInboxCommonModule::class,
        FakeInboxModule::class
    ],
    dependencies = [FakeBaseAppComponent::class]
)
interface FakeInboxComponent : InboxComponent {
    fun injectMembers(inboxFakeDependency: InboxFakeDependency)
}
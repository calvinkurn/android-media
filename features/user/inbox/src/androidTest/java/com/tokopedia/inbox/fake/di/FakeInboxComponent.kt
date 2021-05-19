package com.tokopedia.inbox.fake.di

import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.di.InboxScope
import com.tokopedia.inbox.di.InboxViewModelModule
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.inbox.view.activity.base.InboxTest
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
    fun inject(inboxTest: InboxTest)
}
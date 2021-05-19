package com.tokopedia.inbox.fake.di

import com.tokopedia.inbox.di.InboxCommonModule
import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.di.InboxScope
import com.tokopedia.inbox.di.InboxViewModelModule
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import dagger.Component

@InboxScope
@Component(
        modules = [
            InboxCommonModule::class,
            InboxViewModelModule::class
        ],
        dependencies = [FakeBaseAppComponent::class]
)
interface FakeInboxComponent : InboxComponent {
}
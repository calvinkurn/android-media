package com.tokopedia.inbox.fake.view.activity

import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.fake.di.common.FakeAppModule
import com.tokopedia.inbox.fake.di.DaggerFakeInboxComponent
import com.tokopedia.inbox.fake.di.common.DaggerFakeBaseAppComponent
import com.tokopedia.inbox.fake.view.navigator.FakeInboxFragmentFactory
import com.tokopedia.inbox.view.activity.InboxActivity
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory

class FakeInboxActivity : InboxActivity() {

    override fun createDaggerComponent(): InboxComponent {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
                .fakeAppModule(FakeAppModule(applicationContext))
                .build()
        return DaggerFakeInboxComponent.builder()
                .fakeBaseAppComponent(baseComponent)
                .build()
    }

    override fun createFragmentFactory(): InboxFragmentFactory {
        return FakeInboxFragmentFactory()
    }

    override fun setupToolbarLifecycle() {
        // prevent getting notification from NavToolbar
    }
}
package com.tokopedia.inbox.fake.view.activity

import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.fake.view.navigator.FakeInboxFragmentFactory
import com.tokopedia.inbox.view.activity.InboxActivity
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory

class FakeInboxActivity : InboxActivity() {

    override fun createDaggerComponent(): InboxComponent {
        return InboxTest.inboxComponent!!
    }

    override fun createFragmentFactory(): InboxFragmentFactory {
        return FakeInboxFragmentFactory()
    }

    override fun setupToolbarLifecycle() {
        // prevent getting notification from NavToolbar
    }
}
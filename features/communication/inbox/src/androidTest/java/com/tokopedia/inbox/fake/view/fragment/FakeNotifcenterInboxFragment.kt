package com.tokopedia.inbox.fake.view.fragment

import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.notifcenter.di.NotificationComponent
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment

class FakeNotifcenterInboxFragment: NotificationFragment() {

    override fun generateDaggerComponent(): NotificationComponent {
        return InboxTest.notifcenterComponent!!
    }

    companion object {
        fun create(): FakeNotifcenterInboxFragment {
            return FakeNotifcenterInboxFragment()
        }
    }

}
package com.tokopedia.inbox.fake.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_CHAT
import com.tokopedia.applink.ApplinkConst.Inbox.VALUE_PAGE_NOTIFICATION
import com.tokopedia.inbox.fake.view.fragment.FakeChatListInboxFragment
import com.tokopedia.inbox.fake.view.fragment.FakeNotifcenterInboxFragment
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory

class FakeInboxFragmentFactory(
    private val page: String
) : InboxFragmentFactory {
    override fun createChatListFragment(): Fragment {
        return if (page == VALUE_PAGE_CHAT) {
            FakeChatListInboxFragment.create()
        } else {
            Fragment()
        }
    }

    override fun createNotificationFragment(): Fragment {
        return if (page == VALUE_PAGE_NOTIFICATION) {
            FakeNotifcenterInboxFragment.create()
        } else {
            Fragment()
        }
    }

    override fun createTalkInboxFragment(): Fragment {
        return Fragment()
    }

    override fun createReviewInboxFragment(): Fragment {
        return Fragment()
    }
}
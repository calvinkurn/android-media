package com.tokopedia.inbox.fake.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.inbox.fake.view.fragment.FakeChatListInboxFragment
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory

class FakeInboxFragmentFactory : InboxFragmentFactory {
    override fun createChatListFragment(): Fragment {
        return FakeChatListInboxFragment.create()
    }

    override fun createNotificationFragment(): Fragment {
        return Fragment()
    }

    override fun createTalkInboxFragment(): Fragment {
        return Fragment()
    }

    override fun createReviewInboxFragment(): Fragment {
        return Fragment()
    }
}
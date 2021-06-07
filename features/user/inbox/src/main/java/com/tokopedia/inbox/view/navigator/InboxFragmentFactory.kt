package com.tokopedia.inbox.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment
import com.tokopedia.review.feature.inbox.container.presentation.fragment.ReviewInboxContainerFragment
import com.tokopedia.talk.feature.inbox.presentation.fragment.TalkInboxFragment
import com.tokopedia.topchat.chatlist.fragment.ChatListInboxFragment

interface InboxFragmentFactory {
    fun createChatListFragment(): Fragment
    fun createNotificationFragment(): Fragment
    fun createTalkInboxFragment(): Fragment
    fun createReviewInboxFragment(): Fragment
}

class InboxFragmentFactoryImpl(
    @InboxFragmentType
    private val page: Int,
    private val showBottomNav: Boolean
) : InboxFragmentFactory {

    override fun createChatListFragment(): Fragment {
        return if (eligibleToOpen(InboxFragmentType.CHAT)) {
            ChatListInboxFragment.createFragment()
        } else {
            Fragment()
        }
    }

    override fun createNotificationFragment(): Fragment {
        return if (eligibleToOpen(InboxFragmentType.NOTIFICATION)) {
            NotificationFragment()
        } else {
            Fragment()
        }
    }

    override fun createTalkInboxFragment(): Fragment {
        return if (eligibleToOpen(InboxFragmentType.DISCUSSION)) {
            TalkInboxFragment.createNewInstance()
        } else {
            Fragment()
        }
    }

    override fun createReviewInboxFragment(): Fragment {
        return if (eligibleToOpen(InboxFragmentType.DISCUSSION)) {
            ReviewInboxContainerFragment.createNewInstance()
        } else {
            Fragment()
        }
    }

    private fun eligibleToOpen(
        @InboxFragmentType
        pageToOpen: Int
    ): Boolean {
        return showBottomNav || page == pageToOpen
    }
}
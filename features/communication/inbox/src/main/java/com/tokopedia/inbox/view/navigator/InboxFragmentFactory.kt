package com.tokopedia.inbox.view.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.notifcenter.presentation.fragment.NotificationFragment

interface InboxFragmentFactory {
    fun createNotificationFragment(): Fragment
}

class InboxFragmentFactoryImpl(
    @InboxFragmentType
    private val page: Int,
    private val showBottomNav: Boolean
) : InboxFragmentFactory {

    override fun createNotificationFragment(): Fragment {
        return if (eligibleToOpen(InboxFragmentType.NOTIFICATION)) {
            NotificationFragment()
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

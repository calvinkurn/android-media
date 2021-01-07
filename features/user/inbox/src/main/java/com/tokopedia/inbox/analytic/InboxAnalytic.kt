package com.tokopedia.inbox.analytic

import com.tokopedia.inbox.common.InboxFragmentType
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.analytic.InboxAnalyticCommon.createGeneralEvent
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InboxAnalytic @Inject constructor(
        private val userSession: UserSessionInterface
) {

    class Event private constructor() {
        companion object {
            const val CLICK_INBOX_CHAT = "clickInboxChat"
        }
    }

    class EventCategory private constructor() {
        companion object {
            const val INBOX_PAGE = "inbox page"
        }
    }

    class EventAction private constructor() {
        companion object {
            const val OPEN_INBOX = "open inbox"
            const val CLICK_BOTTOM_NAV_MENU = "click menu at inbox bottom navigation"
        }
    }

    class BusinessUnit private constructor() {
        companion object {
            const val COMMUNICATION = "communication"
        }
    }

    class CurrentSite private constructor() {
        companion object {
            const val MARKETPLACE = "tokopediamarketplace"
        }
    }

    fun trackOpenInbox(
            @InboxFragmentType
            page: Int,
            @RoleType
            role: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.OPEN_INBOX,
                        eventLabel = getEventLabel(page, role),
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackOpenInboxPage(
            @InboxFragmentType
            page: Int,
            @RoleType
            role: Int
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
                getScreenName(page),
                createGeneralEvent(
                        userRole = getRoleString(role),
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE
                )
        )
    }

    fun trackClickBottomNaveMenu(page: Int, role: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.CLICK_BOTTOM_NAV_MENU,
                        eventLabel = getEventLabel(page, role),
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    private fun getScreenName(@InboxFragmentType page: Int): String {
        return when (page) {
            InboxFragmentType.NOTIFICATION -> "/new-inbox/notif"
            InboxFragmentType.CHAT -> "/new-inbox/chat"
            InboxFragmentType.DISCUSSION -> "/new-inbox/diskusi"
            else -> ""
        }
    }

    private fun getEventLabel(
            @InboxFragmentType
            page: Int,
            @RoleType
            role: Int
    ): String {
        val pageString = getPageString(page)
        val roleString = getRoleString(role)
        return "$pageString - $roleString"
    }

    private fun getRoleString(@RoleType role: Int): String {
        return when (role) {
            RoleType.BUYER -> "buyer"
            RoleType.SELLER -> "seller"
            else -> ""
        }
    }

    private fun getPageString(@InboxFragmentType page: Int): String {
        return when (page) {
            InboxFragmentType.NOTIFICATION -> "notif"
            InboxFragmentType.CHAT -> "chat"
            InboxFragmentType.DISCUSSION -> "diskusi"
            else -> ""
        }
    }

}
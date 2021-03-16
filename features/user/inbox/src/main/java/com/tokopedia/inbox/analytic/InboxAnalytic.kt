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
            const val VIEW_INBOX_CHAT_IRIS = "viewInboxChatIris"
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
            const val CLICK_SWITCH_ACCOUNT = "click switch inbox at header"
            const val CHOOSE_SWITCH_ACCOUNT = "click switch inbox role at bottom sheet"
            const val VIEW_INBOX_ONBOARDING = "view new inbox onboarding"
            const val CLICK_INBOX_ONBOARDING = "click new inbox onboarding"
            const val CLOSE_INBOX_ONBOARDING = "click close new inbox"
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

    fun trackClickSwitchAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.CLICK_SWITCH_ACCOUNT,
                        eventLabel = "",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackRoleChanged(@RoleType role: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.CHOOSE_SWITCH_ACCOUNT,
                        eventLabel = "switch to ${getRoleString(role)}",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackShowOnBoardingOnStep(role: Int, currentIndex: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.VIEW_INBOX_CHAT_IRIS,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.VIEW_INBOX_ONBOARDING,
                        eventLabel = "${getRoleStringOnBoarding(role)} - ${currentIndex + 1}",
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackClickOnBoardingCta(role: Int, previousIndex: Int, direction: String) {
        val eventLabel = "${getRoleStringOnBoarding(role)} - ${previousIndex + 1} - $direction"
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.CLICK_INBOX_ONBOARDING,
                        eventLabel = eventLabel,
                        businessUnit = BusinessUnit.COMMUNICATION,
                        currentSite = CurrentSite.MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackDismissOnBoarding(role: Int, currentIndex: Int?) {
        if (currentIndex == null) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = Event.CLICK_INBOX_CHAT,
                        eventCategory = EventCategory.INBOX_PAGE,
                        eventAction = EventAction.CLOSE_INBOX_ONBOARDING,
                        eventLabel = "${getRoleStringOnBoarding(role)} - ${currentIndex + 1}",
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

    private fun getRoleStringOnBoarding(role: Int): String {
        return when (role) {
            RoleType.BUYER -> "buyer_only"
            RoleType.SELLER -> "buyer_seller"
            else -> ""
        }
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
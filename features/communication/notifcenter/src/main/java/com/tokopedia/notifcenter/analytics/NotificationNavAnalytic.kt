package com.tokopedia.notifcenter.analytics

import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.inboxcommon.analytic.InboxAnalyticCommon.createGeneralEvent
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class NotificationNavAnalytic @Inject constructor(
        private val userSession: UserSessionInterface
) {

    fun trackOpenInbox(
            @RoleType
            role: Int
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = "",
                        eventCategory = INBOX_PAGE,
                        eventAction = OPEN_INBOX,
                        eventLabel = getEventLabel(role),
                        businessUnit = COMMUNICATION,
                        currentSite = MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackOpenInboxPage(
            @RoleType
            role: Int
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
                NEW_INBOX_NOTIF,
                createGeneralEvent(
                        userRole = getRoleString(role),
                        businessUnit = COMMUNICATION,
                        currentSite = MARKETPLACE
                )
        )
    }

    fun trackClickSwitchAccount() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = CLICK_INBOX_CHAT,
                        eventCategory = INBOX_PAGE,
                        eventAction = CLICK_SWITCH_ACCOUNT,
                        eventLabel = "",
                        businessUnit = COMMUNICATION,
                        currentSite = MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    fun trackRoleChanged(@RoleType role: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                createGeneralEvent(
                        event = CLICK_INBOX_CHAT,
                        eventCategory = INBOX_PAGE,
                        eventAction = CHOOSE_SWITCH_ACCOUNT,
                        eventLabel = "switch to ${getRoleString(role)}",
                        businessUnit = COMMUNICATION,
                        currentSite = MARKETPLACE,
                        userId = userSession.userId
                )
        )
    }

    private fun getEventLabel(
            @RoleType
            role: Int
    ): String {
        return "$NOTIF - ${getRoleString(role)}"
    }

    private fun getRoleString(@RoleType role: Int): String {
        return when (role) {
            RoleType.BUYER -> BUYER
            RoleType.SELLER -> SELLER
            else -> ""
        }
    }

    companion object {
        private const val COMMUNICATION = "communication"
        private const val MARKETPLACE = "tokopediamarketplace"
        private const val INBOX_PAGE = "inbox page"
        private const val NEW_INBOX_NOTIF = "/new-inbox/notif"

        private const val CLICK_INBOX_CHAT = "clickInboxChat"

        private const val OPEN_INBOX = "open inbox"
        private const val CLICK_SWITCH_ACCOUNT = "click switch inbox at header"
        private const val CHOOSE_SWITCH_ACCOUNT = "click switch inbox role at bottom sheet"

        private const val NOTIF = "notif"
        private const val BUYER = "buyer"
        private const val SELLER = "seller"
    }

}

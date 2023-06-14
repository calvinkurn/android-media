package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.domain.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertMenuCounter
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertMenuCounterGone
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertMenuSectionIsNotOnPosition
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertMenuSectionOnPosition
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertNotificationCounter
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertNotificationCounterGone
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertSellerChatMenu
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertShopInfo
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuRobot
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_KEY
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_TYPE_A
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.ROLLENCE_TYPE_B
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxMenuTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_inbox_menu_with_section() {
        // When
        launchActivity()

        // Then
        assertMenuSectionOnPosition(1)
    }

    @Test
    fun should_show_inbox_menu_without_section() {
        // Given
        setABValue(ROLLENCE_KEY, ROLLENCE_TYPE_B)

        // When
        launchActivity()

        // Then
        assertMenuSectionIsNotOnPosition(1)
        setABValue(ROLLENCE_KEY, ROLLENCE_TYPE_A) // Reset
    }

    @Test
    fun should_show_chat_seller_when_user_has_shop() {
        // When
        launchActivity()

        // Then
        assertSellerChatMenu(position = 3, hasShop = true)
        assertShopInfo(position = 3)
    }

    @Test
    fun should_not_show_chat_seller_when_user_doesnt_have_shop() {
        // Given
        userSessionStub.shopData = null

        // When
        launchActivity()

        // Then
        assertSellerChatMenu(position = 3, hasShop = false)
    }

    @Test
    fun should_show_notification_in_menu() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.chatUnread.unreadBuyer = 5
        }

        // When
        launchActivity()

        // Then
        assertMenuCounter(position = 2, counterText = "5")
    }

    @Test
    fun should_show_max_notification_in_menu() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.chatUnread.unreadBuyer = 100
        }

        // When
        launchActivity()

        // Then
        assertMenuCounter(position = 2, counterText = "99+")
    }

    @Test
    fun should_not_show_counter_in_menu() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.chatUnread.unreadBuyer = 0
        }

        // When
        launchActivity()

        // Then
        assertMenuCounterGone(position = 2)
    }

    @Test
    fun should_show_notification_and_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.notifCenterUnread.notifUnread = "5"
        }

        // When
        launchActivity()

        // Then
        assertNotificationCounter("5")
    }

    @Test
    fun should_show_notification_and_max_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.notifCenterUnread.notifUnread = "100"
        }

        // When
        launchActivity()

        // Then
        assertNotificationCounter("99+")
    }

    @Test
    fun should_not_show_notification_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.notifCenterUnread.notifUnread = "0"
        }

        // When
        launchActivity()

        // Then
        assertNotificationCounterGone()
    }

    @Test
    fun should_show_pull_to_refresh() {
        // When
        launchActivity()

        // Then
        assertNotificationCounterGone()

        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject {
            it.allCounter.notifCenterUnread.notifUnread = "1"
        }

        // When
        MenuRobot.swipeDown()

        // Then
        assertNotificationCounter("1")
    }
}

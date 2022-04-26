package com.tokopedia.inbox.view.activity.notifcenter

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationGeneralRobot.clickNotification
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationGeneralRobot.scrollToProductPosition
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationPinResult.assertBackgroundColor
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationPinResult.assertNotificationPinned
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationPinResult.assertNotificationUnpinned
import com.tokopedia.notifcenter.R
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotifcenterNotificationPinTest: InboxNotifcenterTest() {

    @Test
    fun should_not_show_pin_in_unpinned_notification() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.defaultNotificationsUnread
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationUnpinned(2)
        assertBackgroundColor(2, R.color.notifcenter_dms_unread_notification)
    }

    @Test
    fun should_change_unpinned_notification_background_when_clicked() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.defaultNotificationsUnread
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)
        clickNotification(2)

        // Then
        assertNotificationUnpinned(2)
        assertBackgroundColor(2, null)
    }

    @Test
    fun should_show_pin_in_pinned_notification() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.defaultNotificationsWithPin
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationPinned(2)
        assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    @Test
    fun should_change_pinned_notification_background_when_clicked() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.defaultNotificationsWithPin
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)
        clickNotification(2)

        // Then
        assertNotificationPinned(2)
        assertBackgroundColor(2, null)
    }

    @Test
    fun should_not_show_pin_in_old_list() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.defaultNotificationsUnread
        }
        startInboxActivity()

        //When
        scrollToProductPosition(5)

        // Then
        assertNotificationUnpinned(5)
        assertBackgroundColor(5, null)
    }
}
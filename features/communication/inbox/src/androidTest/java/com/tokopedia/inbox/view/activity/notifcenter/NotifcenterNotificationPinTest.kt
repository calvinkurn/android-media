package com.tokopedia.inbox.view.activity.notifcenter

import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase.Companion.SECTION_TYPE_INFO
import com.tokopedia.inbox.fake.domain.usecase.notifcenter.FakeNotifcenterDetailUseCase.Companion.SECTION_TYPE_PROMO
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.notifcenter.robot.NotificationGeneralRobot.clickChipFilter
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
                notifcenterDetailUseCase.getNotificationPinResponse()
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationPinned(2)
        assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    @Test
    fun should_show_pin_layout_when_get_pinned_notification_in_filtered_notification() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse()
        }
        startInboxActivity()

        //When
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getAlteredFilterPinResponse(
                    typeLink = NOTIFICATION_BANNER,
                    sectionId = SECTION_TYPE_PROMO
                ) //change the response
        }
        clickChipFilter(1)

        // Then
        assertNotificationPinned(0)
        assertBackgroundColor(0, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    @Test
    fun should_change_pinned_notification_background_when_clicked() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse()
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

    @Test
    fun should_hide_pin_expired_when_showing_count_down_in_notif_banner() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse(
                    isShowExpire = true,
                    typeLink = NOTIFICATION_BANNER
                )
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationPinned(2, isShowCountDown = true)
        assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    @Test
    fun should_show_pin_expired_when_not_showing_count_down_in_notif_banner() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse(
                    isShowExpire = false,
                    typeLink = NOTIFICATION_BANNER
                )
        }
        startInboxActivity()

        //When
        scrollToProductPosition(2)

        // Then
        assertNotificationPinned(2, isShowCountDown = false)
        assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    @Test
    fun should_hide_pin_expired_when_showing_count_down_in_notif_banner_after_click_filter() {
        // Given
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse(
                    isShowExpire = true,
                    typeLink = NOTIFICATION_BANNER
                )
        }
        // Show the default notif list
        startInboxActivity()

        // When
        // Click chip filter
        inboxNotifcenterDep.apply {
            // Change the response
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getAlteredFilterPinResponse(
                    typeLink = NOTIFICATION_BANNER,
                    sectionId = SECTION_TYPE_INFO,
                    isShowExpire = false
                )
        }
        clickChipFilter(2)

        // Remove filter (click filter again)
        inboxNotifcenterDep.apply {
            notifcenterDetailUseCase.response =
                notifcenterDetailUseCase.getNotificationPinResponse(
                    isShowExpire = true,
                    typeLink = NOTIFICATION_BANNER
                )
        }
        clickChipFilter(2)

        // Then
        assertNotificationPinned(2, isShowCountDown = true)
        assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
    }

    companion object {
        private const val NOTIFICATION_BANNER = 4
    }
}
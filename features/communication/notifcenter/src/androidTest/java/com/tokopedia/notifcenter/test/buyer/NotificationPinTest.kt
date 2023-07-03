package com.tokopedia.notifcenter.test.buyer

import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
import com.tokopedia.notifcenter.test.robot.detailRobot
import com.tokopedia.notifcenter.test.robot.filterRobot
import com.tokopedia.notifcenter.test.robot.generalRobot
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationPinTest : BaseNotificationTest() {

    @Test
    fun should_not_show_pin_in_unpinned_notification() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().readStatus = 1
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }

        // Then
        detailResult {
            assertNotificationUnpinned(2)
            assertBackgroundColor(2, R.color.notifcenter_dms_unread_notification)
        }
    }

    @Test
    fun should_change_unpinned_notification_background_when_clicked() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().readStatus = 1
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }
        detailRobot {
            clickNotificationAt(2)
        }

        // Then
        detailResult {
            assertNotificationUnpinned(2)
            assertBackgroundColor(2, null)
        }
    }

    @Test
    fun should_show_pin_in_pinned_notification() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 0
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }

        // Then
        detailResult {
            assertNotificationPinned(2)
            assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
        }
    }

    @Test
    fun should_show_pin_layout_when_get_pinned_notification_in_filtered_notification() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 0
        }

        // When
        launchActivity()

        // Given
        // change the response
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
            it.notifcenterDetail.newList.first().sectionId = "promotion"
        }

        // When
        filterRobot {
            clickFilterAt(1)
        }

        // Then
        detailResult {
            assertNotificationPinned(0)
            assertBackgroundColor(0, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
        }
    }

    @Test
    fun should_change_pinned_notification_background_when_clicked() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 0
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }
        detailRobot {
            clickNotificationAt(2)
        }

        // Then
        detailResult {
            assertNotificationPinned(2)
            assertBackgroundColor(2, null)
        }
    }

    @Test
    fun should_not_show_pin_in_old_list() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().readStatus = 1
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(5)
        }

        // Then
        detailResult {
            assertNotificationUnpinned(5)
            assertBackgroundColor(5, null)
        }
    }

    @Test
    fun should_hide_pin_expired_when_showing_count_down_in_notif_banner() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = true
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }

        // Then
        detailResult {
            assertNotificationPinned(2, isShowCountDown = true)
            assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
        }
    }

    @Test
    fun should_show_pin_expired_when_not_showing_count_down_in_notif_banner() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
        }

        // When
        launchActivity()
        generalRobot {
            smoothScrollNotificationTo(2)
        }

        // Then
        detailResult {
            assertNotificationPinned(2, isShowCountDown = false)
            assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
        }
    }

    @Test
    fun should_hide_pin_expired_when_showing_count_down_in_notif_banner_after_click_filter() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = true
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
        }

        // When
        // Show the default notif list
        launchActivity()

        // Given
        // Change the response
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = false
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
            it.notifcenterDetail.newList.first().sectionId = "for_you"
            it.notifcenterDetail.list = it.notifcenterDetail.newList
            it.notifcenterDetail.newList = listOf()
        }

        // When
        // Click chip filter
        filterRobot {
            clickFilterAt(2)
        }

        // Given
        // Change the response again
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList.first().isPinned = true
            it.notifcenterDetail.newList.first().pinnedText = "Di-pin sampai 02 Mei 2022"
            it.notifcenterDetail.newList.first().readStatus = 1
            it.notifcenterDetail.newList.first().isShowExpire = true
            it.notifcenterDetail.newList.first().typeLink = 4 // NOTIFICATION_BANNER
        }

        // When
        // Remove filter (click filter again)
        filterRobot {
            clickFilterAt(2)
        }

        // Then
        detailResult {
            assertNotificationPinned(2, isShowCountDown = true)
            assertBackgroundColor(2, com.tokopedia.unifyprinciples.R.color.Unify_YN50)
        }
    }
}

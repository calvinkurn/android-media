package com.tokopedia.notifcenter.test.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
import com.tokopedia.notifcenter.test.robot.detailRobot
import com.tokopedia.notifcenter.test.robot.filterRobot
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationListTest : BaseNotificationTest() {

    private val TITLE_NEW_LIST = "Terbaru"
    private val TITLE_EARLIER = "Sebelumnya"
    private val TITLE_LOAD_MORE = "Lihat Lebih Banyak"

    @Test
    fun should_show_new_list_title() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.list = listOf() // Remove old list
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertSectionTitleAt(1)
            assertSectionTitleTextAt(1, TITLE_NEW_LIST)
        }
    }

    @Test
    fun should_show_new_list_section_load_more_button_when_new_list_section_has_next_true_and_click() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.list = listOf() // Remove old list
            it.notifcenterDetail.newPaging.hasNext = true
        }

        // When
        launchActivity()
        detailRobot {
            clickLoadMoreAt(3)
        }

        // Then
        detailResult {
            assertNormalNotificationAt(2)
            assertNormalNotificationAt(3)
            assertLoadMoreAt(4)
            assertLoadMoreTitle(4, TITLE_LOAD_MORE)
        }
    }

    @Test
    fun should_hide_new_list_section_load_more_button_when_new_list_section_has_next_false() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.list = listOf() // Remove old list
            it.notifcenterDetail.newPaging.hasNext = false
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertDoesNotHaveLoadMore()
        }
    }

    @Test
    fun should_show_earlier_title_only() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList = listOf() // Remove new list
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertSectionTitleAt(1)
            assertSectionTitleTextAt(1, TITLE_EARLIER)
        }
    }

    @Test
    fun should_show_earlier_section_load_more_button_when_earlier_section_has_next_true_and_click() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList = listOf() // Remove new list
            it.notifcenterDetail.paging.hasNext = true
        }

        // When
        launchActivity()
        detailRobot {
            clickLoadMoreAt(3)
        }

        // Then
        detailResult {
            assertNormalNotificationAt(2)
            assertNormalNotificationAt(3)
            assertLoadMoreAt(4)
            assertLoadMoreTitle(4, TITLE_LOAD_MORE)
        }
    }

    @Test
    fun should_hide_earlier_section_load_more_button_when_earlier_section_has_next_false() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList = listOf() // Remove new list
            it.notifcenterDetail.paging.hasNext = false
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertDoesNotHaveLoadMore()
        }
    }

    @Test
    fun should_show_new_list_title_and_earlier_title_when_both_response_is_not_empty_and_divider() {
        // When
        launchActivity()

        // Then
        detailResult {
            assertSectionTitleAt(1)
            assertSectionTitleTextAt(1, TITLE_NEW_LIST)
            assertSectionTitleAt(4)
            assertSectionTitleTextAt(4, TITLE_EARLIER)
            assertDividerAt(3)
        }
    }

    @Test
    fun should_show_empty_state_with_just_text_when_notifications_is_empty() {
        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList = listOf() // Remove new list
            it.notifcenterDetail.list = listOf() // Remove old list
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertEmptyNotificationWithRecomAt(1)
            assertEmptyNotifStateWithRecomText(
                1,
                R.string.title_notifcenter_empty_with_recom
            )
        }
    }

    @Test
    fun should_show_empty_state_with_illustration_when_notifications_is_empty_with_filter() {
        // When
        launchActivity()

        // Given
        GqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList = listOf() // Remove new list
            it.notifcenterDetail.list = listOf() // Remove old list
        }

        // When
        filterRobot {
            clickFilterAt(0)
        }

        // Then
        detailResult {
            assertEmptyNotificationAt(0)
            assertEmptyNotifStateIllustrationImageVisibility(0, isDisplayed())
            assertEmptyNotifStateIllustrationTitle(0, R.string.notification_empty_message)
            assertEmptyNotifStateIllustrationDescription(
                0,
                R.string.notification_empty_filter_message
            )
        }
    }
}

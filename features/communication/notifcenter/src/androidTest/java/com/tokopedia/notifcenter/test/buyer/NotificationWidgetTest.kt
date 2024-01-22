package com.tokopedia.notifcenter.test.buyer

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
import com.tokopedia.notifcenter.test.robot.detailRobot
import com.tokopedia.notifcenter.test.robot.generalResult
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class NotificationWidgetTest : BaseNotificationTest() {

    @Test
    fun open_notifcenter_as_buyer_show_global_menu_nav_bar() {
        // When
        launchActivity()

        // Then
        generalResult {
            assertNavToolbarGlobal()
        }
    }

    @Test
    fun should_render_widget_message() {
        // Given
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_no_track_history_widget.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject { }
        val msg = gqlResponseStub.notificationDetailResponse.responseObject
            .notifcenterDetail.newList[0].widget.message
        launchActivity()

        // Then
        detailResult {
            assertNotifWidgetVisibility(2, isDisplayed())
            assertNotifWidgetMsg(2, msg)
        }
    }

    @Test
    fun should_render_short_desc_on_notif_widget() {
        // Given
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_no_track_history_widget.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].widget.message = ""
        }
        launchActivity()

        // Then
        detailResult {
            assertNotifWidgetVisibility(2, not(isDisplayed()))
        }
    }

    @Test
    fun should_render_widget_feed_with_multiple_image() {
        // Given
        var title: String? = null
        var desc: String? = null
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_widget_feed_history.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            title = it.notifcenterDetail.newList[0].titleHtml
            desc = it.notifcenterDetail.newList[0].shortDescriptionHtml
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertNotificationWidgetFeed(2, title!!, desc!!)
            assertWidgetFeedMultiple(2)
            assertWidgetFeedToggleButton(2, ViewMatchers.Visibility.VISIBLE)
        }
    }

    @Test
    fun should_render_widget_feed_with_single_image() {
        // Given
        var title: String? = null
        var desc: String? = null
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_widget_feed_history.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].thumbnailImageList = listOf("https://images.tokopedia.net/img/cache/300/tPxBYm/2022/9/21/ac3cb4d9-00e5-43fe-a745-eb007a887c03.jpg")
            title = it.notifcenterDetail.newList[0].titleHtml
            desc = it.notifcenterDetail.newList[0].shortDescriptionHtml
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertNotificationWidgetFeed(2, title!!, desc!!)
            assertWidgetFeedSingle(2)
            assertWidgetFeedToggleButton(2, ViewMatchers.Visibility.VISIBLE)
        }
    }

    @Test
    fun should_render_widget_feed_without_image() {
        // Given
        var title: String? = null
        var desc: String? = null
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_widget_feed_history.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].thumbnailImageList = listOf()
            title = it.notifcenterDetail.newList[0].titleHtml
            desc = it.notifcenterDetail.newList[0].shortDescriptionHtml
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertNotificationWidgetFeed(2, title!!, desc!!)
            assertWidgetNoImage(2)
            assertWidgetFeedToggleButton(2, ViewMatchers.Visibility.VISIBLE)
        }
    }

    @Test
    fun should_render_widget_feed_without_track_history() {
        // Given
        var title: String? = null
        var desc: String? = null
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_widget_feed_history.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            it.notifcenterDetail.newList[0].trackHistory = listOf()
            title = it.notifcenterDetail.newList[0].titleHtml
            desc = it.notifcenterDetail.newList[0].shortDescriptionHtml
        }

        // When
        launchActivity()

        // Then
        detailResult {
            assertNotificationWidgetFeed(2, title!!, desc!!)
            assertWidgetFeedToggleButton(2, ViewMatchers.Visibility.GONE)
        }
    }

    @Test
    fun should_show_feed_history() {
        // Given
        var title: String? = null
        var desc: String? = null
        gqlResponseStub.notificationDetailResponse.filePath =
            "detail/notifcenter_detail_v3_widget_feed_history.json"
        gqlResponseStub.notificationDetailResponse.editAndGetResponseObject {
            title = it.notifcenterDetail.newList[0].titleHtml
            desc = it.notifcenterDetail.newList[0].shortDescriptionHtml
        }

        // When
        launchActivity()
        detailRobot {
            clickToggleHistory(2)
        }

        // Then
        detailResult {
            assertNotificationWidgetFeed(2, title!!, desc!!)
            assertWidgetFeedToggleButton(2, ViewMatchers.Visibility.VISIBLE)
            assertWidgetFeedRv(2, 4)
        }
    }
}

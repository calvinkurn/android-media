package com.tokopedia.notifcenter.test.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.detailResult
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
}

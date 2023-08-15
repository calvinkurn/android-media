package com.tokopedia.notifcenter.test.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.filters.FlakyTest
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel
import com.tokopedia.notifcenter.stub.common.NotificationCacheManagerStub
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.test.base.BaseNotificationTest
import com.tokopedia.notifcenter.test.robot.filterResult
import com.tokopedia.notifcenter.test.robot.filterRobot
import com.tokopedia.notifcenter.test.robot.generalRobot
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NotificationOrderListTest : BaseNotificationTest() {
    @Test
    fun should_show_order_list_when_success_load_order_list_with_no_cache() {
        // When
        launchActivity()

        // Then
        filterResult {
            assertNotificationOrderList(0)
        }
    }

    @Test
    fun should_hide_order_list_when_user_has_notification_filter() {
        // When
        launchActivity()
        filterRobot {
            clickFilterAt(0)
        }

        // Then
        filterResult {
            assertNotNotificationOrderList()
        }
    }

    @Test
    fun should_show_cached_version_order_list_when_cache_data_is_exist() {
        // Given
        GqlResponseStub.notificationOrderListResponse.editAndGetResponseObject {
            it.notifcenterNotifOrderList.list[0].text = "Cache Transaksi"
            it.notifcenterNotifOrderList.list[1].text = "Cache All"
        }
        NotificationCacheManagerStub.saveCache(
            key = "notif_order_list_${RoleType.BUYER}-${userSession.userId}",
            obj = GqlResponseStub.notificationOrderListResponse.responseObject
        )

        // When
        launchActivity()

        // Then
        filterResult {
            assertNotificationOrderList(0)
            assertNotifOrderCardTextAtPosition(0, "Cache Transaksi")
            assertNotifOrderCardTextAtPosition(1, "Cache All")
        }
    }

    @Test
    fun should_update_currently_visible_cached_order_list_with_counter_when_finished_loading_remote_data() {
        // Given
        GqlResponseStub.notificationOrderListResponse.editAndGetResponseObject {
            it.notifcenterNotifOrderList.list[0].text = "Cache Transaksi"
            it.notifcenterNotifOrderList.list[1].text = "Cache All"
        }
        NotificationCacheManagerStub.saveCache(
            key = "notif_order_list_${RoleType.BUYER}-${userSession.userId}",
            obj = GqlResponseStub.notificationOrderListResponse.responseObject
        )
        GqlResponseStub.notificationOrderListResponse.editAndGetResponseObject {} // Reset

        // When
        launchActivity()

        // Then
        filterResult {
            assertNotificationOrderList(0)
            assertNotifOrderCardTextAtPosition(0, "Transaksi berlangsung")
            assertNotifOrderCardTextAtPosition(1, "Lihat semua")
        }
    }

    @Test
    @FlakyTest
    fun should_retain_last_position_when_user_scrolled_down_and_back_to_it() {
        // Given
        GqlResponseStub.notificationOrderListResponse.editAndGetResponseObject {
            val item = it.notifcenterNotifOrderList.list[1]
            val newList = arrayListOf<OrderWidgetUiModel>()
            newList.addAll(it.notifcenterNotifOrderList.list)
            for (i in 0 until 13) {
                newList.add(item)
            }
            it.notifcenterNotifOrderList.list = newList
        }

        // When
        launchActivity()
        filterRobot {
            smoothScrollOrderWidgetTo(14)
        }
        generalRobot {
            smoothScrollNotificationTo(14)
            smoothScrollNotificationTo(0)
        }

        // Then
        filterResult {
            assertOrderWidgetCardAt(
                14,
                isDisplayed()
            )
        }
    }
}

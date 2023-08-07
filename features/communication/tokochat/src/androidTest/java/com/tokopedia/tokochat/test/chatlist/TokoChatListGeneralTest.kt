package com.tokopedia.tokochat.test.chatlist

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokochat.stub.domain.response.ApiResponseModelStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatListTest
import com.tokopedia.tokochat.test.chatlist.robot.generalResult
import com.tokopedia.tokochat.test.chatlist.robot.generalRobot
import com.tokopedia.tokochat_common.R
import org.junit.Test

@UiTest
class TokoChatListGeneralTest : BaseTokoChatListTest() {

    @Test
    fun should_show_chat_list_component() {
        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertPageTitle("Chat Driver")
            assertDriverName(0, "Kartolo")
            assertDriverImageProfile(0)
            assertDriverBadge(0)
            assertDriverTypeOrder(0, "GoFood")
            assertThumbnailMessage(0, "Kak saya otw ya")
            assertBusinessName(0, "Michael Jordan Official Store")
            assertTimeStamp(0, "<1 mnt")
            assertCounter(0, "1")
        }
    }

    @Test
    fun should_show_default_thumbnail_message() {
        // When
        launchChatListActivity()
        Thread.sleep(10000)

        // Then
        generalResult {
            assertDriverName(1, "Lorem ipsum")
            assertDriverImageProfile(1)
            assertDriverBadge(1)
            assertDriverTypeOrder(1, "GoFood")
            assertThumbnailMessage(1, activity.getString(R.string.tokochat_list_default_message))
            assertBusinessName(1, "Lorem ipsum")
            assertNoTimeStamp(1)
            assertNoCounter(1)
        }
    }

    @Test
    fun should_show_99_plus_counter() {
        // Given
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("\"unread_count\": 1", "\"unread_count\": 1000")
            result
        }

        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertCounter(0, "99+")
        }
    }

    @Test
    fun should_not_show_channel_when_expired() {
        // Given
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("9223372036854775807", "0")
            result
        }

        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertDriverName(0, "Lorem ipsum")
        }
    }

    @Test
    fun should_show_jam_timestamp() {
        // Given
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            val newValue = (System.currentTimeMillis() - 3600000).toString()
            result = result.replace("1690441670772", newValue)
            result
        }

        // When
        launchChatListActivity()
        Thread.sleep(10000)

        // Then
        generalResult {
            assertTimeStamp(0, "1 jam")
        }
    }

    @Test
    fun should_show_hari_timestamp() {
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            val newValue = (System.currentTimeMillis() - 86400000).toString()
            result = result.replace("1690441670772", newValue)
            result
        }

        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertDriverName(0, "Kartolo")
            assertTimeStamp(0, "1 hari lalu")
        }
    }

    @Test
    fun should_show_date_month_timestamp() {
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("1690441670772", "1687849670000")
            result
        }

        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertDriverName(1, "Kartolo") // Move to position 1
            assertTimeStamp(1, "27 Jun")
        }
    }

    @Test
    fun should_show_month_year_timestamp() {
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("1690441670772", "1656313670000")
            result
        }

        // When
        launchChatListActivity()

        // Then
        generalResult {
            assertDriverName(1, "Kartolo") // Move to position 1
            assertTimeStamp(1, "Jun 2022")
        }
    }

    @Test
    fun should_go_to_chat_room() {
        // When
        launchChatListActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        generalRobot {
            clickChatListItem(0)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatValueUtil.TOKOFOOD}&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$GOJEK_ORDER_ID_DUMMY"
        )
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_load_more() {
        // When
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            200,
            "channel_list/success_get_channel_list_big_size.json"
        )
        launchChatListActivity()

        // Then
        generalResult {
            assertChatListItemTotal(10)
        }
        Thread.sleep(1000)

        // Given
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            200,
            "channel_list/success_get_load_more_channel_list.json"
        )

        // When
        generalRobot {
            scrollToPosition(10)
        }
        Thread.sleep(300)

        // Then
        generalResult {
            assertChatListItemTotal(11)
        }
    }
}

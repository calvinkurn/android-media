package com.tokopedia.tokochat.test.chatlist

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.common.TokoChatNetworkUtilStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseModelStub
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.test.base.BaseTokoChatListTest
import com.tokopedia.tokochat.test.chatlist.robot.stateResult
import com.tokopedia.tokochat.test.chatlist.robot.stateRobot
import org.junit.Test

@UiTest
class TokoChatStateChatListTest : BaseTokoChatListTest() {

    @Test
    fun should_show_empty_state() {
        // Given
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            200,
            "channel_list/success_get_empty_channel_list.json"
        )

        // When
        launchChatListActivity()

        // Then
        stateResult {
            assertEmptyState(0)
        }
    }

    @Test
    fun should_show_error_network_state() {
        // Given
        (networkUtil as TokoChatNetworkUtilStub).isNetworkAvailable = false
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            403,
            "channel_list/success_get_empty_channel_list.json"
        )

        // When
        launchChatListActivity()

        // Then
        stateResult {
            assertNetworkErrorState()
        }
        (networkUtil as TokoChatNetworkUtilStub).isNetworkAvailable = false
    }

    @Test
    fun should_show_error_general_state() {
        // Given
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            403,
            "channel_list/success_get_empty_channel_list.json"
        )

        // When
        launchChatListActivity()

        // Then
        stateResult {
            assertGlobalErrorState()
        }
    }

    @Test
    fun should_go_to_home() {
        // Given
        ApiResponseStub.channelListResponse = ApiResponseModelStub(
            403,
            "channel_list/success_get_empty_channel_list.json"
        )

        // When
        launchChatListActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        stateRobot {
            clickGoToHome()
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConst.HOME
        )
        Intents.intended(IntentMatchers.hasData(intent.data))
    }
}

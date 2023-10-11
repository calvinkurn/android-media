package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub
import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetIndividualLocalLoad
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaCounter
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaCounterGone
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaGone
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaLocalLoad
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaTitle
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaTotal
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetRobot.clickIndividualLocalLoad
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxWidgetMetaTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_widget_based_on_response() {
        // Given
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu.forEach {
                it.isDynamic = true
            }
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(2)
        assertWidgetMetaTitle(0, "Chat Driver")
        assertWidgetMetaTitle(1, "Tokopedia Care")
    }

    @Test
    fun should_show_widget_based_on_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject { response ->
            response.allCounter.othersUnread.helpUnread = 0
        }
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu.forEach {
                if (it.type == CHATBOT_TYPE) {
                    it.isDynamic = false
                }
            }
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(1)
        assertWidgetMetaTitle(0, "Chat Driver")
    }

    @Test
    fun should_not_show_widget_when_empty() {
        // Given
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu = listOf()
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaGone()
    }

    @Test
    fun should_show_widget_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject { response ->
            response.allCounter.othersUnread.helpUnread = 5
        }
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu.forEach {
                it.isDynamic = true
            }
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaCounter(
            position = 0,
            counterText = "1"
        )
        assertWidgetMetaCounter(
            position = 1,
            counterText = "5"
        )
    }

    @Test
    fun should_show_widget_counter_max() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject { response ->
            response.allCounter.othersUnread.helpUnread = 100
        }
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu.forEach {
                it.isDynamic = true
            }
        }
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("\"unread_count\": 1", "\"unread_count\": 100")
            result
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaCounter(
            position = 1,
            counterText = "99+"
        )
    }

    @Test
    fun should_not_show_widget_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject { response ->
            response.allCounter.othersUnread.helpUnread = 0
        }
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxMenu.widgetMenu.forEach {
                it.isDynamic = true
            }
        }
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("\"unread_count\": 1", "\"unread_count\": 0")
            result
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaCounterGone(0)
        assertWidgetMetaCounterGone(1)
    }

    @Test
    fun should_show_local_load() {
        // Given
        GqlResponseStub.widgetMetaResponse.isError = true

        // When
        launchActivity()

        // Then
        assertWidgetMetaLocalLoad(position = 0)
    }

    @Test
    fun should_not_show_driver_widget_when_active_message_0() {
        // Given
        ApiResponseStub.channelListResponse.responseEditor = {
            var result = it
            result = result.replace("9223372036854775807", "0")
            result = result.replace("9223372036854775806", "0")
            result
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(1)
        assertWidgetMetaTitle(0, "Tokopedia Care")
    }

    @Test
    fun should_not_show_driver_widget_only_when_error_from_sdk() {
        // Given
        ApiResponseStub.channelListResponse.responseCode = 403

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(1)
        assertWidgetMetaTitle(0, "Tokopedia Care")
    }

    @Test
    fun should_show_individual_local_load() {
        // Given
        getAllDriverChannelsUseCase.isError = true

        // When
        launchActivity()

        // Then
        assertWidgetIndividualLocalLoad(position = 0)

        // Given
        getAllDriverChannelsUseCase.isError = false

        // When
        clickIndividualLocalLoad(position = 0)

        // Then
        assertWidgetMetaTotal(2)
        assertWidgetMetaTitle(0, "Chat Driver")
        assertWidgetMetaTitle(1, "Tokopedia Care")
    }
}

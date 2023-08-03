package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaCounter
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaCounterGone
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaGone
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaLocalLoad
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertWidgetMetaTotal
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.CHATBOT_TYPE
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxWidgetMetaTest : BaseUniversalInboxTest() {

    @Test
    fun should_show_widget_based_on_response() {
        // Given
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxWidgetMeta.metaData.forEach {
                it.isDynamic = true
            }
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(2)
    }

    @Test
    fun should_show_widget_based_on_counter() {
        // Given
        GqlResponseStub.counterResponse.editAndGetResponseObject { response ->
            response.allCounter.othersUnread.helpUnread = 0
        }
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxWidgetMeta.metaData.forEach {
                if (it.type == CHATBOT_TYPE) {
                    it.isDynamic = false
                }
            }
        }

        // When
        launchActivity()

        // Then
        assertWidgetMetaTotal(1)
    }

    @Test
    fun should_not_show_widget_when_empty() {
        // Given
        GqlResponseStub.widgetMetaResponse.editAndGetResponseObject { response ->
            response.chatInboxWidgetMeta.metaData = listOf()
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
            response.chatInboxWidgetMeta.metaData.forEach {
                it.isDynamic = true
            }
        }

        // When
        launchActivity()

        // Then
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
            response.chatInboxWidgetMeta.metaData.forEach {
                it.isDynamic = true
            }
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
            response.chatInboxWidgetMeta.metaData.forEach {
                it.isDynamic = true
            }
        }

        // When
        launchActivity()

        // Then
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
}

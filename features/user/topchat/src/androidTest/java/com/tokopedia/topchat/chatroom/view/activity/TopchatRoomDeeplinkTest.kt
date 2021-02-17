package com.tokopedia.topchat.chatroom.view.activity

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.test.application.matcher.isPointingTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TopchatRoomDeeplinkTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val exMessageId = "66961"
    private val exShopId = "77961"
    private val exUserId = "88961"
    private val topchat = "com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity"

    @Test
    fun test_chatroom_external_deeplink_with_msgId() {
        // Given
        val applink = "tokopedia://topchat/$exMessageId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        verifyDeeplink(intent, applink, topchat)
        verifyLastPathSegment(intent, exMessageId)
    }

    @Test
    fun test_chatroom_external_deeplink_with_msgId_formatted() {
        // Given
        val msgId = exMessageId

        // When
        val intent = RouteManager.getIntent(context, ApplinkConst.TOPCHAT, msgId)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, msgId)
    }

    @Test
    fun test_chatroom_external_deeplink_askseller_with_shopId() {
        // Given
        val applink = "tokopedia://topchat/askseller/$exShopId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exShopId)
    }

    @Test
    fun test_chatroom_external_deeplink_askseller_with_shopId_formatted() {
        // Given
        val shopId = exShopId

        // When
        val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER, shopId
        )

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exShopId)
    }

    @Test
    fun test_chatroom_external_deeplink_askseller_with_shopId_and_custom_msg() {
        // Given
        val customMsg = "hi seller"
        val keyCustomMsg = "customMessage"
        val applink = "tokopedia://topchat/askseller/$exShopId?$keyCustomMsg=$customMsg"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exShopId)
        verifyQueryParameter(intent, keyCustomMsg, customMsg)
    }

    @Test
    fun test_chatroom_external_deeplink_askseller_with_shopId_and_custom_msg_formatted() {
        // Given
        val customMsg = "hi seller"
        val shopId = exShopId

        // When
        val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKSELLER_WITH_MSG, shopId, customMsg
        )

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exShopId)
        verifyQueryParameter(intent, "customMessage", customMsg)
    }

    @Test
    fun test_chatroom_external_deeplink_askbuyer_with_userId() {
        // Given
        val applink = "tokopedia://topchat/askbuyer/$exUserId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exUserId)
    }

    @Test
    fun test_chatroom_external_deeplink_askbuyer_with_userId_formatted() {
        // Given
        val userId = exUserId

        // When
        val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKBUYER, userId
        )

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, userId)
    }

    @Test
    fun test_chatroom_external_deeplink_askbuyer_with_userId_and_custom_msg() {
        // Given
        val customMsg = "hi buyer"
        val keyCustomMsg = "customMessage"
        val applink = "tokopedia://topchat/askbuyer/$exUserId?$keyCustomMsg=$customMsg"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exUserId)
        verifyQueryParameter(intent, keyCustomMsg, customMsg)
    }

    @Test
    fun test_chatroom_external_deeplink_askbuyer_with_userId_and_custom_msg_formatted() {
        // Given
        val customMsg = "hi buyer"
        val userId = exUserId

        // When
        val intent = RouteManager.getIntent(
                context, ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG, userId, customMsg
        )

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exUserId)
        verifyQueryParameter(intent, "customMessage", customMsg)
    }

    @Test
    fun test_chatroom_internal_deeplink_with_msgId() {
        // Given
        val applink = "${ApplinkConstInternalGlobal.TOPCHAT}/$exMessageId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, exMessageId)
    }

    @Test
    fun test_chatroom_internal_deeplink_with_msgId_formatted() {
        // Given
        val msgId = exMessageId

        // When
        val intent = RouteManager.getIntent(
                context, ApplinkConstInternalGlobal.TOPCHAT_ROOM, msgId
        )

        // Then
        assertThat(intent, isPointingTo(topchat))
        verifyLastPathSegment(intent, msgId)
    }

    private fun verifyLastPathSegment(
            intent: Intent,
            expectedValue: String
    ) {
        assertThat(intent.data?.lastPathSegment, equalTo(expectedValue))
    }

    private fun verifyQueryParameter(
            intent: Intent,
            key: String,
            expectedValue: String
    ) {
        assertThat(intent.data?.getQueryParameter(key), equalTo(expectedValue))
    }
}
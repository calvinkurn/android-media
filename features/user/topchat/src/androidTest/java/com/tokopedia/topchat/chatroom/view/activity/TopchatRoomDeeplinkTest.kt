package com.tokopedia.topchat.chatroom.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Assert
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
        verifyDeeplink(intent, topchat)
        verifyLastPathSegment(intent, msgId)
    }

    @Test
    fun test_chatroom_external_deeplink_askseller_with_shopId() {
        // Given
        val applink = "tokopedia://topchat/askseller/$exShopId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        verifyDeeplink(intent, applink, topchat)
        verifyLastPathSegment(intent, exShopId)
    }

    @Test
    fun test_chatroom_internal_deeplink_with_msgId() {
        // Given
        val applink = "${ApplinkConstInternalGlobal.TOPCHAT}/$exMessageId"

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        verifyDeeplink(intent, applink, topchat)
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
        verifyDeeplink(intent, topchat)
        verifyLastPathSegment(intent, msgId)
    }

    private fun verifyDeeplink(
            intent: Intent,
            applink: String,
            canonicalName: String
    ) {
        val resolvedActivities = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_ALL)

        val resolverActivityMissing = resolvedActivities.none {
            it.activityInfo.packageName == context.packageName &&
                    it.activityInfo.name == topchat
        }

        if (resolverActivityMissing) {
            Assert.fail("$applink is not resolved for $canonicalName")
        }
    }

    private fun verifyDeeplink(
            intent: Intent,
            canonicalName: String
    ) {
        val resolvedActivities = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_ALL)

        val resolverActivityMissing = resolvedActivities.none {
            it.activityInfo.packageName == context.packageName &&
                    it.activityInfo.name == topchat
        }

        if (resolverActivityMissing) {
            Assert.fail("intent is not resolved for $canonicalName")
        }
    }

    private fun verifyLastPathSegment(
            intent: Intent,
            expectedValue: String
    ) {
        assertThat(intent.data?.lastPathSegment, equalTo(expectedValue))
    }
}
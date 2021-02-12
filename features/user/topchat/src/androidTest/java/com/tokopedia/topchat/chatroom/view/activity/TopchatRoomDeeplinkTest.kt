package com.tokopedia.topchat.chatroom.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
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
    private val topchat = "com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity"

    @Test
    fun test_chatroom_external_deeplink_with_msgId() {
        // Given
        val applink = "tokopedia://topchat/$exMessageId"
        val applinkWithEndTrail = "tokopedia://topchat/$exMessageId/"

        // When
        val intent = RouteManager.getIntent(context, applink)
        val intentWithEndTrail = RouteManager.getIntent(context, applinkWithEndTrail)

        // Then
        verifyDeeplink(intent, applink, topchat)
        verifyDeeplink(intentWithEndTrail, applinkWithEndTrail, topchat)
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
}
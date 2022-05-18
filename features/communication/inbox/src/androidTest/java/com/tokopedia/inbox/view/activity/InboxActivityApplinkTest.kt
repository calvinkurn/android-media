package com.tokopedia.inbox.view.activity

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.isPointingTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@UiTest
class InboxActivityApplinkTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val sellerNotifCenter = "com.tokopedia.notifcenter.presentation.activity.NotificationSellerActivity"

    @Before
    fun setUp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        forceAbNewInbox()
    }

    private fun forceAbNewInbox() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    @Test
    fun should_always_point_to_seller_notification_when_using_old_notification_applink_in_sellerapp() {
        // Given
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        val applinkUri = Uri.parse(ApplinkConst.NOTIFICATION)

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(sellerNotifCenter))
    }
}
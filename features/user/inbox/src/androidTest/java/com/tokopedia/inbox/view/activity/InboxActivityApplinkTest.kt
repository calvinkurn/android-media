package com.tokopedia.inbox.view.activity

import android.net.Uri
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.Inbox.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasQueryParameter
import com.tokopedia.test.application.matcher.isPointingTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
class InboxActivityApplinkTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val inbox = "com.tokopedia.inbox.view.activity.InboxActivity"
    private val sellerNotifCenter = "com.tokopedia.notifcenter.presentation.activity.NotificationSellerActivity"

    @Before
    fun setUp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        forceAbNewInbox()
    }

    private fun forceAbNewInbox() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        applyAbKeyValue(
            RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_NEW_INBOX
        )
    }

    @Test
    fun inbox_external_applink() {
        // Given
        val applink = ApplinkConst.INBOX

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(inbox))
    }

    @Test
    fun inbox_external_applink_with_query_page() {
        // Given
        val applinkUri = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
            appendQueryParameter(
                PARAM_PAGE, VALUE_PAGE_CHAT
            )
        }

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_PAGE, VALUE_PAGE_CHAT))
    }

    @Test
    fun inbox_external_applink_with_role_page() {
        // Given
        val applinkUri = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
            appendQueryParameter(
                PARAM_ROLE, VALUE_ROLE_BUYER
            )
        }

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_ROLE, VALUE_ROLE_BUYER))
    }

    @Test
    fun inbox_external_applink_with_page_and_role_page() {
        // Given
        val applinkUri = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
            appendQueryParameter(
                PARAM_PAGE, VALUE_PAGE_NOTIFICATION
            )
            appendQueryParameter(
                PARAM_ROLE, VALUE_ROLE_SELLER
            )
        }

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_PAGE, VALUE_PAGE_NOTIFICATION))
        assertThat(intent, hasQueryParameter(PARAM_ROLE, VALUE_ROLE_SELLER))
    }

    @Test
    fun should_have_source_query_param_in_external_applink() {
        // Given
        val source = "UOH"
        val applinkUri = Uri.parse(ApplinkConst.INBOX).buildUpon().apply {
            appendQueryParameter(
                PARAM_SOURCE, source
            )
        }

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_SOURCE, source))
    }

    @Test
    fun should_point_to_new_inbox_when_using_old_notification_applink() {
        // Given
        val applinkUri = Uri.parse(ApplinkConst.NOTIFICATION)

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_PAGE, VALUE_PAGE_NOTIFICATION))
        assertThat(intent, hasQueryParameter(PARAM_SHOW_BOTTOM_NAV, "false"))
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

    private fun applyAbKeyValue(key: String, value: String) {
        RemoteConfigInstance.getInstance().abTestPlatform.apply {
            setString(
                key, value
            )
        }
    }
}
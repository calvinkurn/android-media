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
import com.tokopedia.test.application.matcher.hasQueryParameter
import com.tokopedia.test.application.matcher.isPointingTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class InboxActivityApplinkTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val inbox = "com.tokopedia.inbox.view.activity.InboxActivity"
    private val oldNotifcenter = "com.tokopedia.notifcenter.presentation.activity.NotificationActivity"

    @Before
    fun setUp() {
        forceAbNewInbox()
    }

    private fun forceAbNewInbox() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        applyAbKeyValue(
            RollenceKey.KEY_AB_INBOX_REVAMP, RollenceKey.VARIANT_NEW_INBOX
        )
        applyAbKeyValue(
            RollenceKey.NAVIGATION_EXP_TOP_NAV, RollenceKey.NAVIGATION_VARIANT_REVAMP
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
    fun should_point_to_new_inbox_when_whitelisted_ab_old_to_new_notifcenter() {
        // Given
        applyAbKeyValue(
            RollenceKey.KEY_NEW_NOTFICENTER, RollenceKey.VARIANT_NEW_NOTFICENTER
        )
        val applinkUri = Uri.parse(ApplinkConst.NOTIFICATION)

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(inbox))
        assertThat(intent, hasQueryParameter(PARAM_PAGE, VALUE_PAGE_NOTIFICATION))
        assertThat(intent, hasQueryParameter(PARAM_SHOW_BOTTOM_NAV, "false"))
    }

    @Test
    fun should_point_to_old_inbox_when_not_whitelisted_ab_old_to_new_notifcenter() {
        // Given
        applyAbKeyValue(
            RollenceKey.KEY_NEW_NOTFICENTER, RollenceKey.VARIANT_OLD_NOTFICENTER
        )
        val applinkUri = Uri.parse(ApplinkConst.NOTIFICATION)

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(oldNotifcenter))
    }

    @Test
    fun should_always_point_to_old_inbox_when_whitelisted_ab_old_to_new_notifcenter_on_sellerapp() {
        // Given
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        applyAbKeyValue(
            RollenceKey.KEY_NEW_NOTFICENTER, RollenceKey.VARIANT_NEW_NOTFICENTER
        )
        val applinkUri = Uri.parse(ApplinkConst.NOTIFICATION)

        // When
        val intent = RouteManager.getIntent(context, applinkUri.toString())

        // Then
        assertThat(intent, isPointingTo(oldNotifcenter))
    }

    private fun applyAbKeyValue(key: String, value: String) {
        RemoteConfigInstance.getInstance().abTestPlatform.apply {
            setString(
                key, value
            )
        }
    }
}
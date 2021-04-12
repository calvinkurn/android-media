package com.tokopedia.inbox.view.activity

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
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

    @Before
    fun setUp() {
        forceAbNewInbox()
    }

    private fun forceAbNewInbox() {
        RemoteConfigInstance.getInstance().abTestPlatform.apply {
            setString(
                    AbTestPlatform.KEY_AB_INBOX_REVAMP, AbTestPlatform.VARIANT_NEW_INBOX
            )
            setString(
                    AbTestPlatform.NAVIGATION_EXP_TOP_NAV, AbTestPlatform.NAVIGATION_VARIANT_REVAMP
            )
        }
    }

    @Test
    fun test_inbox_external_applink() {
        // Given
        val applink = ApplinkConst.INBOX

        // When
        val intent = RouteManager.getIntent(context, applink)

        // Then
        assertThat(intent, isPointingTo(inbox))
    }
}
package com.tokopedia.notifcenter.test.base

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.notifcenter.stub.common.ActivityScenarioTestRule
import com.tokopedia.notifcenter.ui.seller.NotificationSellerActivity
import org.junit.Rule

abstract class BaseNotificationSellerTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<NotificationSellerActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
}

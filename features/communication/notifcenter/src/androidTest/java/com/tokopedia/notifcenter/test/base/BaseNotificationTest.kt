package com.tokopedia.notifcenter.test.base

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.notifcenter.stub.common.ActivityScenarioTestRule
import com.tokopedia.notifcenter.ui.buyer.NotificationActivity
import org.junit.Rule

abstract class BaseNotificationTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<NotificationActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
}

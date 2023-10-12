package com.tokopedia.notifcenter.test.base

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.notifcenter.stub.common.ActivityScenarioTestRule
import com.tokopedia.notifcenter.view.affiliate.NotificationAffiliateActivity
import org.junit.Rule

abstract class BaseNotificationAffiliateTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<NotificationAffiliateActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
}

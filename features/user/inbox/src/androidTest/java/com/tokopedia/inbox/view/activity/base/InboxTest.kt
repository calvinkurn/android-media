package com.tokopedia.inbox.view.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.fake.di.FakeInboxComponent
import com.tokopedia.inbox.fake.view.activity.FakeInboxActivity
import org.junit.Before
import org.junit.Rule

abstract class InboxTest {
    @get:Rule
    var activityTestRule = IntentsTestRule(
            FakeInboxActivity::class.java, false, false
    )

    protected lateinit var activity: FakeInboxActivity
    protected lateinit var inboxComponent: FakeInboxComponent

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    @Before
    open fun before() {

    }

    protected fun setupInboxActivity(
            isSellerApp: Boolean = false,
            intentModifier: (Intent) -> Unit = {}
    ) {
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
    }

    protected fun waitForIt(timeMillis: Long) {
        Thread.sleep(timeMillis)
    }

}
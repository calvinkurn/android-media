package com.tokopedia.notifcenter.test.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.stub.common.ActivityScenarioTestRule
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.stub.di.NotificationFakeActivityComponentFactory
import com.tokopedia.notifcenter.view.buyer.NotificationActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class BaseNotificationTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<NotificationActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    open fun beforeTest() {
        Intents.init()
        setupDaggerComponent()
        GqlResponseStub.reset()
    }

    private fun setupDaggerComponent() {
        val fakeComponent = NotificationFakeActivityComponentFactory()

        NotificationActivityComponentFactory.instance = fakeComponent
        fakeComponent.notificationComponent.inject(this)
    }

    @After
    open fun afterTest() {
        Intents.release()
    }

    protected fun launchActivity(
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        } else {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        }
        val intent = RouteManager.getIntent(context, ApplinkConst.BUYER_INFO)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
    }

    protected fun stubAllIntents() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}

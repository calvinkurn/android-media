package com.tokopedia.notifcenter.test.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.notifcenter.di.NotificationActivityComponentFactory
import com.tokopedia.notifcenter.stub.common.ActivityScenarioTestRule
import com.tokopedia.notifcenter.stub.data.repository.FakeTopAdsRepository
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.notifcenter.stub.di.NotificationFakeActivityComponentFactory
import com.tokopedia.notifcenter.view.seller.NotificationSellerActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseNotificationSellerTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<NotificationSellerActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Inject
    lateinit var topAdsRepository: FakeTopAdsRepository

    @Inject
    @ApplicationContext
    lateinit var gqlResponseStub: GqlResponseStub

    @Before
    open fun beforeTest() {
        Intents.init()
        setupDaggerComponent()
        reset()
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
        intentModifier: (Intent) -> Unit = {}
    ) {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        val intent = RouteManager.getIntent(context, ApplinkConst.BUYER_INFO)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
        stubAllIntents()
    }

    private fun stubAllIntents() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun reset() {
        gqlResponseStub.reset()
        topAdsRepository.isError = false
        topAdsRepository.response = topAdsRepository.defaultResponse
    }
}

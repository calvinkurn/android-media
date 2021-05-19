package com.tokopedia.inbox.view.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.fake.di.DaggerFakeInboxComponent
import com.tokopedia.inbox.fake.di.FakeInboxComponent
import com.tokopedia.inbox.fake.di.common.DaggerFakeBaseAppComponent
import com.tokopedia.inbox.fake.di.common.FakeAppModule
import com.tokopedia.inbox.fake.di.common.FakeBaseAppComponent
import com.tokopedia.inbox.fake.view.activity.FakeInboxActivity
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class InboxTest {
    @get:Rule
    var activityTestRule = IntentsTestRule(
            FakeInboxActivity::class.java, false, false
    )

    protected lateinit var activity: FakeInboxActivity

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
        get() = InstrumentationRegistry
                .getInstrumentation().context.applicationContext

    @Before
    open fun before() {
        setupGraphqlMockResponse(InboxModelConfig())
        setupDaggerBaseComponent()
        setupInboxDaggerComponent()
    }

    @After
    fun tearDown() {
        baseComponent = null
        inboxComponent = null
    }

    private fun setupDaggerBaseComponent() {
        baseComponent = DaggerFakeBaseAppComponent.builder()
                .fakeAppModule(FakeAppModule(applicationContext))
                .build()
    }

    private fun setupInboxDaggerComponent() {
        inboxComponent = DaggerFakeInboxComponent.builder()
                .fakeBaseAppComponent(baseComponent)
                .build()
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

    companion object {
        var baseComponent: FakeBaseAppComponent? = null
        var inboxComponent: FakeInboxComponent? = null
    }
}

/**
 * Dummy model config for query notification from
 * [com.tokopedia.searchbar.navigation_component.NavToolbar]
 * prevent it from requesting to the internet
 */
class InboxModelConfig : MockModelConfig() {
    override fun createMockModel(context: Context): MockModelConfig {
        addMockResponse(
                "query Notification", "", FIND_BY_CONTAINS
        )
        return this
    }
}

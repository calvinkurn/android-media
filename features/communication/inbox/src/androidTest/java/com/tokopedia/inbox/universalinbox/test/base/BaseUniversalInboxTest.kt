package com.tokopedia.inbox.universalinbox.test.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.universalinbox.di.UniversalInboxActivityComponentFactory
import com.tokopedia.inbox.universalinbox.stub.common.ActivityScenarioTestRule
import com.tokopedia.inbox.universalinbox.stub.common.UserSessionStub
import com.tokopedia.inbox.universalinbox.stub.common.util.FakeAbTestPlatformImpl
import com.tokopedia.inbox.universalinbox.stub.di.UniversalInboxFakeActivityComponentFactory
import com.tokopedia.inbox.universalinbox.stub.domain.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.UniversalInboxActivity
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseUniversalInboxTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<UniversalInboxActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var abTestPlatform: UniversalInboxAbPlatform

    protected val userSessionStub by lazy {
        userSession as UserSessionStub
    }

    @Before
    open fun beforeTest() {
        Intents.init()
        setupDaggerComponent()
        GqlResponseStub.reset()
    }

    @After
    open fun afterTest() {
        Intents.release()
    }

    private fun setupDaggerComponent() {
        val fakeComponent = UniversalInboxFakeActivityComponentFactory()
        UniversalInboxActivityComponentFactory.instance = fakeComponent
        fakeComponent.universalInboxComponent.inject(this)
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
        val intent = Intent(context, UniversalInboxActivity::class.java)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
    }

    protected fun setABValue(key: String, value: String) {
        (abTestPlatform as FakeAbTestPlatformImpl).editValue(key, value)
    }
}

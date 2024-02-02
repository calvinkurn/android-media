package com.tokopedia.shareexperience.test.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shareexperience.data.di.component.ShareExComponentFactoryProvider
import com.tokopedia.shareexperience.data.mapper.ShareExChannelMapper
import com.tokopedia.shareexperience.stub.ShareExDummyActivity
import com.tokopedia.shareexperience.stub.common.ShareExChannelMapperStub
import com.tokopedia.shareexperience.stub.common.UserSessionStub
import com.tokopedia.shareexperience.stub.data.GqlResponseStub
import com.tokopedia.shareexperience.stub.di.ShareExFakeComponentFactory
import com.tokopedia.test.application.environment.ActivityScenarioTestRule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class ShareExBaseTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<ShareExDummyActivity>()

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @Inject
    lateinit var userSession: UserSessionInterface

    protected val userSessionStub by lazy {
        userSession as UserSessionStub
    }

    @Inject
    lateinit var channelMapper: ShareExChannelMapper

    protected val channelMapperStub by lazy {
        channelMapper as ShareExChannelMapperStub
    }

    @Before
    open fun beforeTest() {
        Intents.init()
        resetResponses()
        setupDaggerComponent()
    }

    @After
    open fun afterTest() {
        Intents.release()
    }

    private fun setupDaggerComponent() {
        val fakeComponent = ShareExFakeComponentFactory()

        ShareExComponentFactoryProvider.instance = fakeComponent
        fakeComponent.shareExComponentFactory.inject(this)
    }

    private fun resetResponses() {
        runBlocking(Dispatchers.Main) {
            GqlResponseStub.reset()
        }
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
        val intent = Intent(context, ShareExDummyActivity::class.java)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
    }

    protected fun stubAllIntents() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}

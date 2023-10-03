package com.tokopedia.inbox.universalinbox.test.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.database.ConversationsDatabase
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.inbox.universalinbox.di.UniversalInboxActivityComponentFactory
import com.tokopedia.inbox.universalinbox.stub.common.ActivityScenarioTestRule
import com.tokopedia.inbox.universalinbox.stub.common.BabbleCourierClientStub
import com.tokopedia.inbox.universalinbox.stub.common.ConversationsPreferencesStub
import com.tokopedia.inbox.universalinbox.stub.common.MockWebServerDispatcher
import com.tokopedia.inbox.universalinbox.stub.common.UserSessionStub
import com.tokopedia.inbox.universalinbox.stub.common.util.FakeAbTestPlatformImpl
import com.tokopedia.inbox.universalinbox.stub.data.response.ApiResponseStub
import com.tokopedia.inbox.universalinbox.stub.data.response.GqlResponseStub
import com.tokopedia.inbox.universalinbox.stub.di.UniversalInboxFakeActivityComponentFactory
import com.tokopedia.inbox.universalinbox.stub.di.tokochat.UniversalInboxTokoChatNetworkModuleStub.PORT_NUMBER
import com.tokopedia.inbox.universalinbox.stub.domain.UniversalInboxGetAllDriverChannelsUseCaseStub
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.util.toggle.UniversalInboxAbPlatform
import com.tokopedia.inbox.universalinbox.view.UniversalInboxActivity
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetComponentProvider
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
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
    lateinit var conversationsPreferences: ConversationsPreferencesStub

    @Inject
    lateinit var babbleCourierClient: BabbleCourierClientStub

    @Inject
    lateinit var tokochatRepository: TokoChatRepository

    @ActivityScope
    @Inject
    lateinit var abTestPlatform: UniversalInboxAbPlatform

    @Inject
    @TokoChatQualifier
    lateinit var okhttpClient: OkHttpClient

    @Inject
    @TokoChatQualifier
    lateinit var database: ConversationsDatabase

    @Inject
    lateinit var getAllDriverChannelsUseCase: UniversalInboxGetAllDriverChannelsUseCaseStub

    protected val userSessionStub by lazy {
        userSession as UserSessionStub
    }

    private lateinit var mockWebServer: MockWebServer
    private val mockWebServerDispatcher = MockWebServerDispatcher()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Before
    open fun beforeTest() {
        AndroidThreeTen.init(applicationContext)
        Intents.init()
        resetResponses()
        setupDaggerComponent()
        setupConversationAndCourier()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okhttpClient)
        IdlingRegistry.getInstance().register(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage
        )
        setMockWebServer()
        resetDatabase()
    }

    @After
    open fun afterTest() {
        mockWebServer.shutdown()
        removeConversationAndCourier()
        Intents.release()
        IdlingRegistry.getInstance().unregister(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage
        )
    }

    private fun setupDaggerComponent() {
        val fakeComponent = UniversalInboxFakeActivityComponentFactory()

        UniversalInboxActivityComponentFactory.instance = fakeComponent
        fakeComponent.universalInboxComponent.inject(this)

        RecommendationWidgetComponentProvider.setRecommendationComponent(
            fakeComponent.recommendationWidgetComponent
        )
    }

    private fun setMockWebServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start(PORT_NUMBER)
        mockWebServer.dispatcher = mockWebServerDispatcher
    }

    private fun resetResponses() {
        runBlocking(Dispatchers.Main) {
            ApiResponseStub.reset()
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
        val intent = Intent(context, UniversalInboxActivity::class.java)
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
        generalRobot {
            scrollToPosition(0)
        }
    }

    protected fun setABValue(key: String, value: String) {
        (abTestPlatform as FakeAbTestPlatformImpl).editValue(key, value)
    }

    protected fun stubAllIntents() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupConversationAndCourier() {
        runBlocking(Dispatchers.Main) {
            babbleCourierClient.init(USER_ID_DUMMY)
            babbleCourierClient.setClientId(USER_ID_DUMMY)
            conversationsPreferences.setProfileDetails(USER_ID_DUMMY)
            tokochatRepository.initConversationRepository()
        }
    }

    private fun resetDatabase() {
        runBlocking(Dispatchers.Main) {
            idlingResourceDatabaseMessage.increment()
            database.messageDao().deleteAll()
            val result = database.channelDao().getChannelIds(listOf(ChannelType.GroupBooking.name))
            result.forEach {
                database.channelDao().deleteChannelById(it)
            }
            idlingResourceDatabaseMessage.decrement()
        }
    }

    private fun removeConversationAndCourier() {
        conversationsPreferences.clearProfileData()
    }

    companion object {
        const val USER_ID_DUMMY = "835a69de-577e-4881-bf1d-4e3eed13c643"
        val idlingResourceDatabaseMessage = CountingIdlingResource(
            "tokochat-database-message"
        )
    }
}

package com.tokopedia.tokochat.test.base

import android.content.Context
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents
import androidx.test.platform.app.InstrumentationRegistry
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.database.ConversationsDatabase
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.tokochat.common.util.TokoChatCacheManager
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.stub.common.BabbleCourierClientStub
import com.tokopedia.tokochat.stub.common.ConversationsPreferencesStub
import com.tokopedia.tokochat.stub.common.MockWebServerDispatcher
import com.tokopedia.tokochat.stub.common.TokoChatCacheManagerStub
import com.tokopedia.tokochat.stub.di.TokoChatNetworkModuleStub.PORT_NUMBER
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatRoomUseCaseStub
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import javax.inject.Inject

abstract class BaseTokoChatTest {

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var mockWebServer: MockWebServer
    private val mockWebServerDispatcher = MockWebServerDispatcher()
    private lateinit var okHttp3IdlingResource: OkHttp3IdlingResource

    @Inject
    @TokoChatQualifier
    lateinit var okhttpClient: OkHttpClient

    @Inject
    lateinit var conversationsPreferences: ConversationsPreferencesStub

    @Inject
    lateinit var babbleCourierClient: BabbleCourierClientStub

    @Inject
    lateinit var tokochatRepository: TokoChatRepository

    @Inject
    lateinit var tokoChatRoomUseCase: TokoChatRoomUseCaseStub

    @Inject
    @TokoChatQualifier
    lateinit var database: ConversationsDatabase

    @Inject
    lateinit var cacheManager: TokoChatCacheManager

    @Inject
    lateinit var remoteConfig: RemoteConfig

    @Inject
    lateinit var networkUtil: TokoChatNetworkUtil

    abstract fun setupDaggerComponent()

    @Before
    open fun before() {
        AndroidThreeTen.init(applicationContext)
        Intents.init()
        resetResponses()
        setupDaggerComponent()
        setupConversationAndCourier()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okhttpClient)
        IdlingRegistry.getInstance().register(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage,
            idlingResourcePrepareDb
        )
        setMockWebServer()
        resetDatabase()
        removeDummyCache()
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
        removeConversationAndCourier()
        Intents.release()
        IdlingRegistry.getInstance().unregister(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage,
            idlingResourcePrepareDb
        )
    }

    private fun setMockWebServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start(PORT_NUMBER)
        mockWebServer.dispatcher = mockWebServerDispatcher
    }

    private fun resetResponses() {
        runBlocking {
            ApiResponseStub.getInstance().reset()
            GqlResponseStub.reset()
        }
    }

    private fun setupConversationAndCourier() {
        runBlocking {
            babbleCourierClient.init(USER_ID_DUMMY)
            babbleCourierClient.setClientId(USER_ID_DUMMY)
            conversationsPreferences.setProfileDetails(USER_ID_DUMMY)
            tokochatRepository.initConversationRepository()
        }
    }

    private fun removeConversationAndCourier() {
        conversationsPreferences.clearProfileData()
    }

    protected open fun resetDatabase() {
        idlingResourceDatabaseMessage.increment()
        database.messageDao().deleteAll()
        val result = database.channelDao().getChannelIds(listOf(ChannelType.GroupBooking.name))
        result.forEach {
            database.channelDao().deleteChannelById(it)
        }
        database.userDao().deleteAll()
        database.channelDao().deleteChannelById(CHANNEL_ID_DUMMY)
        idlingResourceDatabaseMessage.decrement()
        Thread.sleep(1000)
    }

    private fun removeDummyCache() {
        (cacheManager as TokoChatCacheManagerStub).resetAll()
    }

    companion object {
        const val USER_ID_DUMMY = "835a69de-577e-4881-bf1d-4e3eed13c643"
        const val GOJEK_ORDER_ID_DUMMY = "F-68720537282"
        const val TKPD_ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
        const val CHANNEL_ID_DUMMY = "b0c80252-c6a6-40f1-a3ce-a9894a32ac6d"

        val idlingResourceDatabaseMessage = CountingIdlingResource(
            "tokochat-database-message"
        )
        val idlingResourcePrepareDb = CountingIdlingResource(
            "tokochat-prepare-db"
        )
    }
}

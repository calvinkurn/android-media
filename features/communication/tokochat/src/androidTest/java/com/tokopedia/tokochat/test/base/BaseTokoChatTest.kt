package com.tokopedia.tokochat.test.base

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.gojek.conversations.ConversationsRepository
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.database.ConversationsDatabase
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.tokochat.stub.common.BabbleCourierClientStub
import com.tokopedia.tokochat.stub.common.ConversationsPreferencesStub
import com.tokopedia.tokochat.stub.common.MockWebServerDispatcher
import com.tokopedia.tokochat.stub.common.util.RecyclerViewUtil
import com.tokopedia.tokochat.stub.di.DaggerTokoChatComponentStub
import com.tokopedia.tokochat.stub.di.TokoChatComponentStub
import com.tokopedia.tokochat.stub.di.TokoChatCourierConversationModule
import com.tokopedia.tokochat.stub.di.base.DaggerFakeBaseAppComponent
import com.tokopedia.tokochat.stub.di.base.FakeAppModule
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatChannelUseCaseStub
import com.tokopedia.tokochat.stub.view.TokoChatActivityStub
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseTokoChatTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        TokoChatActivityStub::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private val mockWebServer = MockWebServer()
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
    lateinit var tokoChatChannelUseCase: TokoChatChannelUseCaseStub

    @Inject
    @TokoChatQualifier
    lateinit var database: ConversationsDatabase

    protected lateinit var activity: TokoChatActivityStub

    @Before
    open fun before() {
        AndroidThreeTen.init(applicationContext)
        resetResponses()
        setupDaggerComponent()
        setupConversationAndCourier()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okhttpClient)
        IdlingRegistry.getInstance().register(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage,
            idlingResourceDatabaseChannel,
            idlingResourceGroupBooking,
            idlingResourcePrepareDb
        )
        mockWebServer.start(8090)
        resetDatabase()
        shouldWaitForChatHistory = true
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
        removeConversationAndCourier()
        tokoChatComponent = null
        IdlingRegistry.getInstance().unregister(
            okHttp3IdlingResource,
            idlingResourceDatabaseMessage,
            idlingResourceDatabaseChannel,
            idlingResourceGroupBooking,
            idlingResourcePrepareDb
        )
    }

    private fun resetResponses() {
        runBlocking(Dispatchers.Main) {
            ApiResponseStub.reset()
            GqlResponseStub.reset()
        }
    }

    private fun setupConversationAndCourier() {
        runBlocking(Dispatchers.Main) {
            babbleCourierClient.init(USER_ID_DUMMY)
            babbleCourierClient.setClientId(USER_ID_DUMMY)
            conversationsPreferences.setProfileDetails(USER_ID_DUMMY)
            tokochatRepository.initConversationRepository()
        }
    }

    private fun removeConversationAndCourier() {
        conversationsPreferences.clearProfileData()
    }

    protected fun launchChatRoomActivity(
        gojekOrderId: String = GOJEK_ORDER_ID_DUMMY,
        tkpdOrderId: String = TKPD_ORDER_ID_DUMMY,
        source: String = TokoChatValueUtil.TOKOFOOD,
        pushNotifTemplateKey: String? = null,
        isSellerApp: Boolean = false,
        isFromTokoFoodPostPurchase: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        mockWebServer.dispatcher = mockWebServerDispatcher
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val intent = Intent().apply {
            putExtra(
                ApplinkConst.TokoChat.ORDER_ID_GOJEK,
                gojekOrderId
            )
            putExtra(
                ApplinkConst.TokoChat.ORDER_ID_TKPD,
                tkpdOrderId
            )
            putExtra(
                ApplinkConst.TokoChat.PARAM_SOURCE,
                source
            )
            putExtra(
                ApplinkConst.TokoChat.IS_FROM_TOKOFOOD_POST_PURCHASE,
                isFromTokoFoodPostPurchase
            )
            pushNotifTemplateKey?.let {
                putExtra(
                    com.tokopedia.tokochat.util.TokoChatValueUtil.NOTIFCENTER_NOTIFICATION_TEMPLATE_KEY,
                    it
                )
            }
        }
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
        Thread.sleep(2000) // Waiting for all network call and databases from conversation sdk
    }

    private fun setupDaggerComponent() {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        tokoChatComponent = DaggerTokoChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context))
            .tokoChatCourierConversationModule(
                TokoChatCourierConversationModule(applicationContext as Application)
            )
            .build()
        tokoChatComponent!!.inject(this)
    }

    protected fun getTokoChatAdapter(): TokoChatBaseAdapter {
        return RecyclerViewUtil.getAdapter(
            activity = activity,
            recyclerViewId = com.tokopedia.tokochat_common.R.id.tokochat_chatroom_rv
        )
    }

    protected open fun resetDatabase() {
        runBlocking(Dispatchers.Main) {
            try {
                idlingResourceDatabaseMessage.increment()
                database.messageDao().deleteAll()
            } finally {
                idlingResourceDatabaseMessage.decrement()
            }
        }
    }

    protected fun resetChannelDetailDatabase() {
        runBlocking(Dispatchers.Main) {
            try {
                idlingResourceDatabaseChannel.increment()
                database.channelDao().deleteChannelById(CHANNEL_ID_DUMMY)
            } finally {
                idlingResourceDatabaseChannel.decrement()
            }
        }
    }

    // Need to prepare before class,
    // in case the database is not ready after been deleted from other test class
    private fun prepareDatabase() {
        ConversationsRepository.instance!!.initGroupBookingChat(
            GOJEK_ORDER_ID_DUMMY,
            TokoChatViewModel.TOKOFOOD_SERVICE_TYPE,
            object : ConversationsGroupBookingListener {
                override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {
                    if (!idlingResourcePrepareDb.isIdleNow) {
                        idlingResourcePrepareDb.decrement()
                    }
                }
                override fun onGroupBookingChannelCreationStarted() {
                    idlingResourcePrepareDb.increment()
                }
                override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {
                    if (!idlingResourcePrepareDb.isIdleNow) {
                        idlingResourcePrepareDb.decrement()
                    }
                }
            },
            OrderChatType.Unknown
        )
    }

    companion object {
        const val USER_ID_DUMMY = "835a69de-577e-4881-bf1d-4e3eed13c643"
        const val GOJEK_ORDER_ID_DUMMY = "F-68720537282"
        const val TKPD_ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
        const val CHANNEL_ID_DUMMY = "b0c80252-c6a6-40f1-a3ce-a9894a32ac6d"

        var tokoChatComponent: TokoChatComponentStub? = null
        val idlingResourceDatabaseMessage = CountingIdlingResource(
            "tokochat-database-message"
        )
        val idlingResourceDatabaseChannel = CountingIdlingResource(
            "tokochat-database-channel"
        )
        val idlingResourceGroupBooking = CountingIdlingResource(
            "tokochat-groupbooking"
        )
        val idlingResourcePrepareDb = CountingIdlingResource(
            "tokochat-prepare-db"
        )

        var shouldWaitForChatHistory: Boolean = true
    }
}

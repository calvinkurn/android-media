package com.tokopedia.tokochat.test

import android.content.Context
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.tokochat.tokochat_config_common.di.module.TokoChatConfigContextModule
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.common.BabbleCourierClientStub
import com.tokopedia.tokochat.stub.common.ConversationsPreferencesStub
import com.tokopedia.tokochat.stub.common.MockWebServerDispatcher
import com.tokopedia.tokochat.stub.di.DaggerTokoChatComponentStub
import com.tokopedia.tokochat.stub.di.TokoChatComponentStub
import com.tokopedia.tokochat.stub.view.TokoChatActivityStub
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@UiTest
class TokoChatTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        TokoChatActivityStub::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    protected val applicationContext: Context
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

    protected open lateinit var activity: TokoChatActivityStub

    @Before
    open fun before() {
        setupDaggerComponent()
        setupConversationAndCourier()
        okHttp3IdlingResource = OkHttp3IdlingResource.create("okhttp", okhttpClient)
        IdlingRegistry.getInstance().register(okHttp3IdlingResource)
        mockWebServer.start(8080)
    }

    @After
    open fun tearDown() {
        removeConversationAndCourier()
        tokoChatComponent = null
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(okHttp3IdlingResource)
    }

    private fun setupConversationAndCourier() {
        babbleCourierClient.init(USER_ID_DUMMY)
        babbleCourierClient.setClientId(USER_ID_DUMMY)
        conversationsPreferences.setProfileDetails(USER_ID_DUMMY)
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
    }

    @Test
    fun test123()
    {
        Thread.sleep(10000)
        mockWebServer.dispatcher = mockWebServerDispatcher
        launchChatRoomActivity()
        Thread.sleep(20000)
    }

    private fun setupDaggerComponent() {
        tokoChatComponent = DaggerTokoChatComponentStub.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context))
            .build()
        tokoChatComponent!!.inject(this)
    }

    companion object {
        const val USER_ID_DUMMY = "9075737"
        const val GOJEK_ORDER_ID_DUMMY = "F-68720537282"
        const val TKPD_ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
        const val CHANNEL_ID_DUMMY = "b0c80252-c6a6-40f1-a3ce-a9894a32ac6d"

        var tokoChatComponent: TokoChatComponentStub? = null
    }
}

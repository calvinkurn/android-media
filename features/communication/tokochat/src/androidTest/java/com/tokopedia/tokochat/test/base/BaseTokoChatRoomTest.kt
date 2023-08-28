package com.tokopedia.tokochat.test.base

import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokochat.common.view.chatroom.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.stub.common.ActivityScenarioTestRule
import com.tokopedia.tokochat.stub.common.util.RecyclerViewUtil
import com.tokopedia.tokochat.stub.di.DaggerTokoChatUserConsentComponentStub
import com.tokopedia.tokochat.stub.di.TokoChatFakeActivityComponentFactory
import com.tokopedia.tokochat.view.chatroom.TokoChatActivity
import com.tokopedia.tokochat.view.chatroom.TokoChatFragment
import com.tokopedia.usercomponents.userconsent.common.UserConsentComponentProvider
import com.tokopedia.usercomponents.userconsent.di.UserConsentComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import com.tokopedia.tokochat_common.R as tokochat_commonR

abstract class BaseTokoChatRoomTest : BaseTokoChatTest() {

    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<TokoChatActivity>()

    protected lateinit var activity: TokoChatActivity

    @Before
    override fun before() {
        super.before()
        enableAttachmentMenu()
    }

    override fun setupDaggerComponent() {
        val fakeComponent = TokoChatFakeActivityComponentFactory()
        TokoChatActivityComponentFactory.instance = fakeComponent
        fakeComponent.tokoChatComponent.inject(this)

        val userConsentComponent: UserConsentComponent =
            DaggerTokoChatUserConsentComponentStub.builder()
                .fakeBaseAppComponent(fakeComponent.baseComponent)
                .build()
        UserConsentComponentProvider.setUserConsentComponent(userConsentComponent)
    }

    protected fun getTokoChatAdapter(): TokoChatBaseAdapter {
        return RecyclerViewUtil.getAdapter(
            activity = activity,
            recyclerViewId = tokochat_commonR.id.tokochat_chatroom_rv
        )
    }

    private fun enableAttachmentMenu() {
        remoteConfig.setString(
            TokoChatFragment.TOKOCHAT_ATTACHMENT_MENU,
            "true"
        )
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
        val intent = RouteManager.getIntent(
            context,
            "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=$source&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$gojekOrderId&${ApplinkConst.TokoChat.ORDER_ID_TKPD}=$tkpdOrderId"
        ).apply {
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
        activityScenarioRule.launchActivity(intent)
        activityScenarioRule.scenario!!.onActivity {
            activity = it
        }
        Thread.sleep(300)
    }
}

package com.tokopedia.tokochat.test.base

import android.content.Intent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.stub.common.ActivityScenarioTestRule
import com.tokopedia.tokochat.stub.di.TokoChatFakeActivityComponentFactory
import com.tokopedia.tokochat.view.chatlist.TokoChatListActivity
import org.junit.Rule

abstract class BaseTokoChatListTest : BaseTokoChatTest() {

    @get:Rule
    var activityScenarioRule = ActivityScenarioTestRule<TokoChatListActivity>()

    protected lateinit var activity: TokoChatListActivity

    override fun setupDaggerComponent() {
        val fakeComponent = TokoChatFakeActivityComponentFactory()
        TokoChatActivityComponentFactory.instance = fakeComponent
        fakeComponent.tokoChatComponent.inject(this)
    }

    protected fun launchChatListActivity(
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        }
        val intent = RouteManager.getIntent(
            context,
            ApplinkConst.TOKO_CHAT_LIST
        )
        intentModifier(intent)
        activityScenarioRule.launchActivity(intent)
        activityScenarioRule.scenario!!.onActivity {
            activity = it
        }
        Thread.sleep(300)
    }
}

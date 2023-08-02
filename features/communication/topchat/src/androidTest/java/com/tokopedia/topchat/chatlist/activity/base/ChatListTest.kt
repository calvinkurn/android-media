package com.tokopedia.topchat.chatlist.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.view.adapter.viewholder.ChatItemListViewHolder.Companion.ROLLENCE_MVC_ICON
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout.Companion.BROADCAST_FAB_LABEL_PREF_NAME
import com.tokopedia.topchat.chatlist.view.widget.BroadcastButtonLayout.Companion.BROADCAST_FAB_LABEL_ROLLENCE_KEY
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.topchat.stub.chatlist.data.GqlResponseStub
import com.tokopedia.topchat.stub.chatlist.di.ChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.FakeActivityComponentFactory
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetOperationalInsightUseCaseStub
import com.tokopedia.user.session.UserSessionInterface
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class ChatListTest {
    @get:Rule
    var activityTestRule = IntentsTestRule(
        ChatListActivity::class.java,
        false,
        false
    )

    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    lateinit var fakeComponent: FakeActivityComponentFactory

    @Inject
    protected lateinit var chatListUseCase: GetChatListMessageUseCaseStub

    @Inject
    protected lateinit var chatWhitelistFeatureUseCase: GetChatWhitelistFeatureStub

    @Inject
    protected lateinit var getOperationalInsightUseCase: GetOperationalInsightUseCaseStub

    @Inject
    protected lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var abTestPlatform: AbTestPlatform

    @Inject
    lateinit var cacheManager: TopchatCacheManager

    protected lateinit var activity: ChatListActivity

    protected val exEmptyChatListPojo = ChatListPojo()
    protected var exSize2ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
        "success_get_chat_list.json",
        ChatListPojo::class.java
    )
    protected var exSize5ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
        "success_get_chat_list_size_5.json",
        ChatListPojo::class.java
    )
    protected var exBroadcastChatListPojo: ChatListPojo = AndroidFileUtil.parse(
        "broadcast/success_get_chat_list_broadcast.json",
        ChatListPojo::class.java
    )

    @Before
    fun setup() {
        GqlResponseStub.reset()
        fakeComponent = FakeActivityComponentFactory()
        ActivityComponentFactory.instance = fakeComponent
        fakeComponent.chatListComponent.inject(this)
    }

    @After
    fun tearDown() {
        chatListComponentStub = null
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
    }

    protected fun startChatListActivity(
        isSellerApp: Boolean = false,
        intentModifier: (Intent) -> Unit = {}
    ) {
        if (isSellerApp) {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        } else {
            GlobalConfig.APPLICATION_TYPE = GlobalConfig.CONSUMER_APPLICATION
        }
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    protected fun setRollenceMVCIcon(isActive: Boolean) {
        abTestPlatform.setString(ROLLENCE_MVC_ICON, if (isActive) ROLLENCE_MVC_ICON else "")
    }

    protected fun setLastSeenTab(isSellerTab: Boolean) {
        context.getSharedPreferences(
            ChatTabCounterViewModel.PREF_CHAT_LIST_TAB,
            Context.MODE_PRIVATE
        )
            .edit()
            .apply {
                putInt(
                    ChatTabCounterViewModel.KEY_LAST_POSITION,
                    if (isSellerTab) Int.ZERO else Int.ONE
                )
                apply()
            }
    }

    protected fun setLabelNew(value: Boolean) {
        cacheManager.saveState(
            "${BROADCAST_FAB_LABEL_PREF_NAME}_${userSession.userId}",
            value
        )
    }

    protected fun setRollenceLabelNew(isActive: Boolean) {
        abTestPlatform.setString(
            BROADCAST_FAB_LABEL_ROLLENCE_KEY,
            if (isActive) BROADCAST_FAB_LABEL_ROLLENCE_KEY else ""
        )
    }

    companion object {
        var chatListComponentStub: ChatListComponentStub? = null
    }
}

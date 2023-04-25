package com.tokopedia.topchat.chatlist.activity.base

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.di.ActivityComponentFactory
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.chatlist.view.activity.ChatListActivity
import com.tokopedia.topchat.stub.chatlist.di.ChatListComponentStub
import com.tokopedia.topchat.stub.chatlist.di.FakeActivityComponentFactory
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetOperationalInsightUseCaseStub
import com.tokopedia.topchat.stub.common.UserSessionStub
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
    protected val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    lateinit var fakeComponent: FakeActivityComponentFactory

    @Inject
    protected lateinit var chatListUseCase: GetChatListMessageUseCaseStub

    @Inject
    protected lateinit var chatWhitelistFeatureUseCase: GetChatWhitelistFeatureStub

    @Inject
    protected lateinit var getOperationalInsightUseCase: GetOperationalInsightUseCaseStub

    @Inject
    protected lateinit var userSession: UserSessionInterface

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

    @Before
    fun setup() {
        setupDaggerComponent()
        setUserSessionData()
    }

    private fun setupDaggerComponent() {
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
        }
        val intent = Intent()
        intentModifier(intent)
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    private fun setUserSessionData() {
        (userSession as UserSessionStub).hasShopStub = true
        (userSession as UserSessionStub).shopNameStub = "Toko Rifqi 123"
        (userSession as UserSessionStub).nameStub = "Rifqi MF 123"
    }

    companion object {
        var chatListComponentStub: ChatListComponentStub? = null
    }
}

package com.tokopedia.topchat.chatlist.activity.base

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.domain.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.activity.ChatListActivityStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatListMessageUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatWhitelistFeatureStub
import com.tokopedia.topchat.stub.common.UserSessionStub
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class ChatListTest {
    @get:Rule
    var mActivityTestRule = IntentsTestRule(ChatListActivityStub::class.java, true, true)

    protected lateinit var chatListUseCase: GetChatListMessageUseCaseStub
    protected lateinit var chatNotificationUseCase: GetChatNotificationUseCaseStub
    protected lateinit var chatWhitelistFeatureUseCase: GetChatWhitelistFeatureStub
    protected lateinit var userSession: UserSessionStub
    protected lateinit var activity: ChatListActivityStub

    protected val exEmptyChatListPojo = ChatListPojo()
    protected var exSize2ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
        "success_get_chat_list.json",
        ChatListPojo::class.java
    )
    protected var exSize5ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
        "success_get_chat_list_size_5.json",
        ChatListPojo::class.java
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        chatListUseCase = GetChatListMessageUseCaseStub()
        chatNotificationUseCase = GetChatNotificationUseCaseStub()
        chatWhitelistFeatureUseCase = GetChatWhitelistFeatureStub()
        userSession = mActivityTestRule.activity.userSessionInterface
        activity = mActivityTestRule.activity
    }
}
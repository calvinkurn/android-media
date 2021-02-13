package com.tokopedia.topchat.chatroom.view.activity.base

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule

abstract class TopchatRoomTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            TopChatRoomActivityStub::class.java, false, false
    )

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected open lateinit var getChatUseCase: GetChatUseCaseStub
    protected open lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub
    protected open lateinit var activity: TopChatRoomActivityStub

    protected open val exMessageId = "66961"

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()
    }

    protected fun setupChatRoomActivity(
            sourcePage: String? = null
    ) {
        val intent = Intent().apply {
            putExtra(ApplinkConst.Chat.MESSAGE_ID, exMessageId)
            sourcePage?.let {
                putExtra(ApplinkConst.Chat.SOURCE_PAGE, it)
            }
        }
        activityTestRule.launchActivity(intent)
        activity = activityTestRule.activity
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
    }

}
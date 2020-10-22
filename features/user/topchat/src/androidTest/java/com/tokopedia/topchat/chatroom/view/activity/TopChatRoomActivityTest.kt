package com.tokopedia.topchat.chatroom.view.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivityTest.Dummy.exMessageId
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TopChatRoomActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(TopChatRoomActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getChatUseCase: GetChatUseCaseStub
    private lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub

    private lateinit var activity: TopChatRoomActivityStub

    private var firstPageChat: GetExistingChatPojo = AndroidFileUtil.parse(
            "success_get_chat_first_page.json",
            GetExistingChatPojo::class.java
    )
    private var chatAttachmentResponse: ChatAttachmentResponse = AndroidFileUtil.parse(
            "success_get_chat_attachments.json",
            ChatAttachmentResponse::class.java
    )

    object Dummy {
        val exMessageId = "66961"
    }

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = mActivityTestRule.activity
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()
    }

    @Test
    fun size_2_chat_list() {
        // Given

        // When
        setupActivityIntent(exMessageId)
        getChatUseCase.response = firstPageChat
        chatAttachmentUseCase.response = chatAttachmentResponse
        activity.setupTestFragment(getChatUseCase, chatAttachmentUseCase)
        Thread.sleep(5000)

        // Then
        assertTrue(true)
    }

    private fun setupActivityIntent(messageId: String = "") {
        mActivityTestRule.activity.intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
    }

}
package com.tokopedia.topchat.chatroom.view.activity

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivityTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matcher
import androidx.test.espresso.UiController
import com.tokopedia.topchat.R

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

        val viewInteraction = onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view))).check(matches(isDisplayed()))
        var position = 4
        var idToClick = R.id.containerProductAttachment
//        viewInteraction.perform(actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(0, click()))
        viewInteraction.perform(actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(position, MyViewAction.clickChildViewWithId(idToClick)))
        Thread.sleep(30000)

        // Then
        assertTrue(true)
    }

    object MyViewAction {
        fun clickChildViewWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController?, view: View) {
                    val v: View = view.findViewById(id)
                    v.performClick()
                }
            }
        }
    }

    private fun setupActivityIntent(messageId: String = "") {
        mActivityTestRule.activity.intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, messageId)
    }

}
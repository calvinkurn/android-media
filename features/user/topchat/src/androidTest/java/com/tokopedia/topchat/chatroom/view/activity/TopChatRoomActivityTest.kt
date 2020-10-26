package com.tokopedia.topchat.chatroom.view.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.processor.beta.AddToCartBundler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.core.analytics.container.GTMAnalytics
import com.tokopedia.network.interceptor.akamai.map
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivityTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
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

        RemoteConfigInstance.getInstance().abTestPlatform.setString(TopchatProductAttachmentViewHolder.AB_TEST_KEY, TopchatProductAttachmentViewHolder.VARIANT_A)

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, TestAnalytic::class.java)
    }

    class TestAnalytic(context: Context) : ContextAnalytics(context){
        val map = mutableMapOf<String?, Bundle?>()
        val seen = mutableSetOf<String>()

        override fun sendGeneralEvent(value: MutableMap<String, Any>?) {
            seen.add(value?.get("event")?.toString() ?:"")
        }

        override fun sendGeneralEvent(event: String?, category: String?, action: String?, label: String?) {
            seen.add(event?:"")
        }

        override fun sendEvent(eventName: String?, eventValue: MutableMap<String, Any>?) {
            seen.add(eventName?:"")
        }

        override fun sendEnhanceEcommerceEvent(value: MutableMap<String, Any>?) {
            seen.add(value?.get("event")?.toString() ?:"")
        }

        override fun sendScreenAuthenticated(screenName: String?) {
            seen.add(screenName?:"")
        }

        override fun sendScreenAuthenticated(screenName: String?, customDimension: MutableMap<String, String>?) {

        }

        override fun sendScreenAuthenticated(screenName: String?, shopID: String?, shopType: String?, pageType: String?, productId: String?) {

        }

        override fun sendEnhanceEcommerceEvent(eventName: String?, value: Bundle?) {
            map[eventName] = value
        }

        fun isCalled(screenName: String?) = seen.contains(screenName)
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

        // working click
        val viewInteraction = onView(AllOf.allOf(isDisplayed(), withId(R.id.recycler_view))).check(matches(isDisplayed()))
        var position = 1
        var idToClick = R.id.tv_occ
////        viewInteraction.perform(actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(0, click()))
        viewInteraction.perform(actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(position, MyViewAction.clickChildViewWithId(idToClick)))
        // working click

        // recyclerview parent container recyclerview + id recyclerview
//        onView(AllOf.allOf(withParent(withId(R.id.rv_container)), withId(R.id.recycler_view))).check(matches(isDisplayed()))

//        Thread.sleep(30000)

//        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(1, R.id.product_name)).check(matches(withText(containsString("3"))))


//        onView(AllOf.allOf(withParent(withId(R.id.recycler_view)), withId(R.id.cl_msg_container))).check(matches(isDisplayed()))

        Thread.sleep(5000)

        val testAnalytic  = TrackApp.getInstance().gtm as TestAnalytic
        assertEquals("this ${AddToCartBundler.KEY} should not be null",true, testAnalytic.map.containsKey(AddToCartBundler.KEY))





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
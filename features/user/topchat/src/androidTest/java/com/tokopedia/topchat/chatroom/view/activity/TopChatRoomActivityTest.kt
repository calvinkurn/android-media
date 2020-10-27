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
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.processor.beta.AddToCartBundler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.core.analytics.container.GTMAnalytics
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.oneclickcheckout.common.utils.ResourceUtils.getJsonFromResource
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.test.application.environment.interceptor.mock.MockInterceptor
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivityTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatProductAttachmentViewHolder
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.track.TrackApp
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

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDbSource = GtmLogDBSource(context)

    object Dummy {
        val exMessageId = "66961"
    }

    private fun setupGraphqlMockResponse() {
        val mockModelConfig = createMockModelConfig()
        mockModelConfig.createMockModel(context)

        val testInterceptors = listOf(MockInterceptor(mockModelConfig))

        GraphqlClient.reInitRetrofitWithInterceptors(testInterceptors, context)
    }

    private fun createMockModelConfig(): MockModelConfig {
        return object : MockModelConfig() {
            override fun createMockModel(context: Context): MockModelConfig {
                addMockResponse("add_to_cart_occ", getJsonFromResource("occ/occ_success.json"), FIND_BY_CONTAINS)
                return this
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = mActivityTestRule.activity
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()

        gtmLogDbSource.deleteAll().subscribe()
        setupGraphqlMockResponse()

        RemoteConfigInstance.getInstance().abTestPlatform.setString(TopchatProductAttachmentViewHolder.AB_TEST_KEY, TopchatProductAttachmentViewHolder.VARIANT_A)

        TrackApp.getInstance().registerImplementation(TrackApp.GTM, TestAnalytic::class.java)
    }

    class TestAnalytic(context: Context) : GTMAnalytics(context) {
        val map = mutableMapOf<String?, Bundle?>()
        val seen = mutableSetOf<String>()

        override fun sendGeneralEvent(value: MutableMap<String, Any>?) {
            super.sendGeneralEvent(value)
            seen.add(value?.get("event")?.toString() ?: "")
        }

        override fun sendGeneralEvent(event: String?, category: String?, action: String?, label: String?) {
            super.sendGeneralEvent(event, category, action, label)
            seen.add(event ?: "")
        }

        override fun sendEvent(eventName: String?, eventValue: MutableMap<String, Any>?) {
            super.sendEvent(eventName, eventValue)
            seen.add(eventName ?: "")
        }

        override fun sendEnhanceEcommerceEvent(value: MutableMap<String, Any>?) {
            super.sendEnhanceEcommerceEvent(value)
            seen.add(value?.get("event")?.toString() ?: "")
        }

        override fun sendScreenAuthenticated(screenName: String?) {
            super.sendScreenAuthenticated(screenName)
            seen.add(screenName ?: "")
        }

        override fun sendEnhanceEcommerceEvent(eventName: String?, value: Bundle?) {
            super.sendEnhanceEcommerceEvent(eventName, value)
            map[eventName] = value
        }

        fun isCalled(eventName: String?) = seen.contains(eventName) || map.containsKey(eventName)
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
        viewInteraction.perform(actionOnItemAtPosition<TopchatProductAttachmentViewHolder>(position, MyViewAction.clickChildViewWithId(idToClick)))

        Thread.sleep(3000)

        val testAnalytic = TrackApp.getInstance().gtm as TestAnalytic
        assertEquals("this ${AddToCartBundler.KEY} should not be null", true, testAnalytic.map.containsKey(AddToCartBundler.KEY))
        assertEquals("this ${AddToCartBundler.KEY} should not be null", true, testAnalytic.isCalled(AddToCartBundler.KEY))

        Thread.sleep(3000)
        val discomQuery = "tracker/user/topchat/topchat_room_occ_p0.json"
        assertThat(getAnalyticsWithQuery(gtmLogDbSource, context, discomQuery), hasAllSuccess()) // use assertThat from Hamcrest is recommended

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
package com.tokopedia.topchat.chatlist.activity


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.activity.ChatListActivityStub
import com.tokopedia.topchat.stub.chatlist.usecase.ChatListGraphqlUseCase
import com.tokopedia.topchat.stub.common.UserSessionStub
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ChatListActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(ChatListActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var chatListUseCase: ChatListGraphqlUseCase
    private lateinit var userSession: UserSessionStub
    private lateinit var activity: ChatListActivityStub

    private val exEmptyChatListPojo = ChatListPojo()
    private var exSize2ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
            "success_get_chat_list.json",
            ChatListPojo::class.java
    )
    private var exSize5ChatListPojo: ChatListPojo = AndroidFileUtil.parse(
            "success_get_chat_list_size_5.json",
            ChatListPojo::class.java
    )

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
//        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        chatListUseCase = ChatListGraphqlUseCase()
        userSession = mActivityTestRule.activity.userSessionInterface
        activity = mActivityTestRule.activity
    }

    @Test
    fun empty_chat_list_buyer_only() {
        // Given
        userSession.hasShopStub = false
        chatListUseCase.response = exEmptyChatListPojo
//        coEvery { chatListUseCase.executeOnBackground() } returns exEmptyChatListPojo
//        every { chatListUseCase.execute(captureLambda(), any()) } answers {
//            lambda<(ChatListPojo) -> Unit>().invoke(exEmptyChatListPojo)
////            onSuccess.invoke(exSize2ChatListPojo)
//        }

        // When
        activity.setupTestFragment(chatListUseCase)
        Thread.sleep(2500)
    }

    @Test
    fun size_2_chat_list_buyer_only() {
        // Given
        userSession.hasShopStub = false
        chatListUseCase.response = exSize2ChatListPojo
//        coEvery { chatListUseCase.execute(captureLambda(), any()) } coAnswers {
//            val onSuccess = lambda<(ChatListPojo) -> Unit>()
////            onSuccess.invoke(exSize2ChatListPojo)
//            onSuccess.invoke(exEmptyChatListPojo)
//        }
//        every { chatListUseCase.execute(captureLambda(), any()) } just Runs

//        // When
        activity.setupTestFragment(chatListUseCase)
        Thread.sleep(2500)

        // Then
//        val tabView = onView(
//                allOf(childAtPosition(
//                        childAtPosition(
//                                withId(R.id.tl_chat_list),
//                                0),
//                        1),
//                        isDisplayed()))
//        tabView.perform(click())
//
//        val constraintLayout = onView(
//                allOf(childAtPosition(
//                        allOf(withId(R.id.recycler_view),
//                                childAtPosition(
//                                        withId(R.id.swipe_refresh_layout),
//                                        0)),
//                        0),
//                        isDisplayed()))
//        constraintLayout.perform(click())
    }

    @Test
    fun empty_chat_list_seller_buyer() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = "Toko Rifqi"
        userSession.nameStub = "Rifqi MF"
        chatListUseCase.response = exEmptyChatListPojo
//        every { chatListUseCase.execute(captureLambda(), any()) } answers {
//            val onSuccess = lambda<(ChatListPojo) -> Unit>()
//            onSuccess.invoke(exEmptyChatListPojo)
//        }
//        every { chatListUseCase.execute(captureLambda(), any()) } just Runs
//
//        // When
        activity.setupTestFragment(chatListUseCase)
        Thread.sleep(2500)
    }

    @Test
    fun size_5_chat_list_seller_buyer() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = "Toko Rifqi 123"
        userSession.nameStub = "Rifqi MF 123"
        chatListUseCase.response = exSize5ChatListPojo
//        every { chatListUseCase.execute(captureLambda(), any()) } answers {
//            val onSuccess = lambda<(ChatListPojo) -> Unit>()
////            onSuccess.invoke(exSize5ChatListPojo)
//            onSuccess.invoke(exEmptyChatListPojo)
//        }
//        every { chatListUseCase.execute(captureLambda(), any()) } just Runs
//
//        // When
        activity.setupTestFragment(chatListUseCase)
        Thread.sleep(2500)
    }

//    private fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {
//
//        return object : TypeSafeMatcher<View>() {
//            override fun describeTo(description: Description) {
//                description.appendText("Child at position $position in parent ")
//                parentMatcher.describeTo(description)
//            }
//
//            public override fun matchesSafely(view: View): Boolean {
//                val parent = view.parent
//                return parent is ViewGroup && parentMatcher.matches(parent)
//                        && view == parent.getChildAt(position)
//            }
//        }
//    }
}

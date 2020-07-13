package com.tokopedia.topchat.chatlist.activity


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.activity.ChatListActivityStub
import com.tokopedia.topchat.stub.common.UserSessionStub
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ChatListActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(ChatListActivityStub::class.java, true, true)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userSession: UserSessionStub
    private lateinit var activity: ChatListActivityStub

//    private var chatListUseCase: GraphqlUseCase<ChatListPojo> = mockk(relaxed = true)

//    object Dummy {
//        val exChatListPojo = ChatListPojo()
//    }

    @Before
    fun setup() {
//        MockKAnnotations.init(this)
        userSession = mActivityTestRule.activity.userSessionInterface
        activity = mActivityTestRule.activity
    }

    @Test
    fun test_user_session() {
        // Given
        userSession.hasShopStub = false
//        every { chatListUseCase.execute(captureLambda(), any()) } answers {
//            val onSuccess = lambda<(ChatListPojo) -> Unit>()
//            onSuccess.invoke(ChatListPojo())
//        }

        // When
        var chatListUseCase: Any = mockk(relaxed = true)
        activity.setupTestFragment()
        Thread.sleep(5000)
    }

    @Test
    fun chatListActivityTest() {
        userSession.hasShopStub = true
//        var chatListUseCase: GraphqlUseCase<ChatListPojo> = mockk(relaxed = true)
        activity.setupTestFragment()
        Thread.sleep(5000)
//        val scenario = launchFragmentInContainer<ChatTabListFragment>()
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

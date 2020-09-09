package com.tokopedia.topchat.chatlist.activity


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.matchers.RecyclerViewItemCountAssertion
import com.tokopedia.topchat.stub.chatlist.activity.ChatListActivityStub
import com.tokopedia.topchat.stub.chatlist.usecase.ChatListGraphqlUseCase
import com.tokopedia.topchat.stub.common.UserSessionStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers.not
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

        // When
        activity.setupTestFragment(chatListUseCase)

        // Then
        onView(withId(R.id.thumbnail))
                .check(matches(isDisplayed()))
        onView(withId(R.id.title))
                .check(matches(withText("Belum ada chat, nih")))
        onView(withId(R.id.subtitle))
                .check(matches(withText("Coba ngobrol dengan teman penjual, yuk!")))
        onView(withId(R.id.btnCta))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun size_2_chat_list_buyer_only() {
        // Given
        userSession.hasShopStub = false
        chatListUseCase.response = exSize2ChatListPojo

        // When
        activity.setupTestFragment(chatListUseCase)

        // Then
        onView(withId(R.id.recycler_view))
                .check(RecyclerViewItemCountAssertion(2))
    }

    @Test
    fun empty_chat_list_seller_buyer() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = "Toko Rifqi"
        userSession.nameStub = "Rifqi MF"
        chatListUseCase.response = exEmptyChatListPojo

        // When
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

        // When
        activity.setupTestFragment(chatListUseCase)
        Thread.sleep(2500)
    }

}

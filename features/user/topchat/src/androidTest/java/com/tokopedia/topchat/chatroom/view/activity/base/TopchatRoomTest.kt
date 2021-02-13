package com.tokopedia.topchat.chatroom.view.activity.base

import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topchat.R
import com.tokopedia.topchat.idling.FragmentTransactionIdle
import com.tokopedia.topchat.stub.chatroom.usecase.ChatAttachmentUseCaseStub
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.chatroom.websocket.RxWebSocketUtilStub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.TimeUnit

abstract class TopchatRoomTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
            TopChatRoomActivityStub::class.java, false, false
    )

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected open lateinit var getChatUseCase: GetChatUseCaseStub
    protected open lateinit var chatAttachmentUseCase: ChatAttachmentUseCaseStub
    protected open lateinit var websocket: RxWebSocketUtilStub
    protected open lateinit var activity: TopChatRoomActivityStub
    protected open lateinit var fragmentTransactionIdling: FragmentTransactionIdle

    protected open val exMessageId = "66961"

    protected val RV_DELAY = TimeUnit.SECONDS.toMillis(3)

    @ExperimentalCoroutinesApi
    @Before
    open fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        getChatUseCase = GetChatUseCaseStub()
        chatAttachmentUseCase = ChatAttachmentUseCaseStub()
        websocket = RxWebSocketUtilStub()
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
        fragmentTransactionIdling = FragmentTransactionIdle(
                activity.supportFragmentManager,
                TopChatRoomActivityStub.TAG
        )
    }

    protected fun waitForFragmentResumed() {
        IdlingRegistry.getInstance().register(fragmentTransactionIdling)
        Espresso.onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
        IdlingRegistry.getInstance().unregister(fragmentTransactionIdling)
    }

    protected fun waitForIt(millis: Long) {
        Thread.sleep(millis)
    }

    protected fun inflateTestFragment() {
        activity.setupTestFragment(
                getChatUseCase, chatAttachmentUseCase, websocket
        )
        waitForFragmentResumed()
    }

}
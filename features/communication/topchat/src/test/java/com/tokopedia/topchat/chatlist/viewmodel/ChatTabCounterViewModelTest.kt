package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationUseCase
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatTabCounterViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatTabCounterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var getChatNotification: GetChatNotificationUseCase

    private lateinit var viewModel: ChatTabCounterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChatTabCounterViewModel(
            getChatNotification,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun should_give() {
        // Given
        val expectedResponse = NotificationsPojo().apply {
            notification.chat.unreadsUser = 1
            notification.chat.unreadsSeller = 1
        }
        coEvery {
            getChatNotification(any())
        } returns expectedResponse

        // When
        viewModel.queryGetNotifCounter("testShopId")

        // Then
        Assert.assertEquals(
            expectedResponse,
            (viewModel.chatNotifCounter.value as Success).data
        )
    }

    @Test
    fun should_give_error() {
        // Given
        val dummyThrowable = Throwable("Oops!")
        coEvery {
            getChatNotification(any())
        } throws dummyThrowable

        // When
        viewModel.queryGetNotifCounter("testShopId")

        // Then
        Assert.assertEquals(
            dummyThrowable.message,
            (viewModel.chatNotifCounter.value as Fail).throwable.message
        )
    }
}

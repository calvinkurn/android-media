package com.tokopedia.topchat.chatlist.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import app.cash.turbine.testIn
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListLastVisitedTabUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationCounterUseCase
import com.tokopedia.topchat.chatlist.view.TopChatListAction
import com.tokopedia.topchat.chatlist.view.viewmodel.ChatTabCounterViewModel
import com.tokopedia.topchat.common.data.TopChatResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatTabCounterViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var getChatNotification: GetChatNotificationCounterUseCase

    @RelaxedMockK
    lateinit var getChatLastVisitedTab: GetChatListLastVisitedTabUseCase

    private lateinit var viewModel: ChatTabCounterViewModel

    private val testShopId = "shopId"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ChatTabCounterViewModel(
            getChatNotification,
            getChatLastVisitedTab,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `refresh counter, get notification data`() {
        runTest {
            // Given
            val expectedResponse = NotificationsPojo().apply {
                notification.chat.unreadsUser = 1
                notification.chat.unreadsSeller = 1
            }
            val notificationFlow = MutableSharedFlow<TopChatResult<NotificationsPojo>>()
            coEvery {
                getChatNotification.observe()
            } returns notificationFlow

            coEvery {
                getChatNotification.refreshCounter(any())
            } coAnswers {
                notificationFlow.emit(TopChatResult.Loading)
                notificationFlow.emit(TopChatResult.Success(expectedResponse))
            }

            viewModel.chatListNotificationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // Then inital state
                val initialState = awaitItem()
                Assert.assertEquals(
                    false,
                    initialState.isLoading
                )
                Assert.assertEquals(
                    0,
                    initialState.unreadBuyer
                )
                Assert.assertEquals(
                    0,
                    initialState.unreadSeller
                )

                // When Refresh Counter
                viewModel.processAction(TopChatListAction.RefreshCounter(testShopId))

                // Then loading state
                val loadingState = awaitItem()
                Assert.assertEquals(
                    true,
                    loadingState.isLoading
                )
                Assert.assertEquals(
                    0,
                    loadingState.unreadBuyer
                )
                Assert.assertEquals(
                    0,
                    loadingState.unreadSeller
                )

                // Then updated state
                val updatedState = awaitItem()
                Assert.assertEquals(
                    false,
                    updatedState.isLoading
                )
                Assert.assertEquals(
                    1,
                    updatedState.unreadBuyer
                )
                Assert.assertEquals(
                    1,
                    updatedState.unreadSeller
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `refresh counter, get error`() {
        runTest {
            // Given
            val expectedError = Throwable("Oops!")
            val notificationFlow = MutableSharedFlow<TopChatResult<NotificationsPojo>>()
            coEvery {
                getChatNotification.observe()
            } returns notificationFlow

            coEvery {
                getChatNotification.refreshCounter(any())
            } coAnswers {
                notificationFlow.emit(TopChatResult.Loading)
                notificationFlow.emit(TopChatResult.Error(expectedError))
            }

            val notificationUiState = viewModel.chatListNotificationUiState.testIn(this)
            val errorUiState = viewModel.errorUiState.testIn(this)

            // When initial state
            viewModel.setupViewModelObserver()
            // When Refresh Counter
            viewModel.processAction(TopChatListAction.RefreshCounter(testShopId))

            // Then
            notificationUiState.skipItems(2) // Initial & Loading
            // Then updated state
            val updatedState = notificationUiState.awaitItem()
            Assert.assertEquals(
                false,
                updatedState.isLoading
            )
            Assert.assertEquals(
                0,
                updatedState.unreadBuyer
            )
            Assert.assertEquals(
                0,
                updatedState.unreadSeller
            )

            val errorState = errorUiState.awaitItem()
            Assert.assertEquals(
                "Oops!",
                errorState.error?.first?.message
            )

            notificationUiState.cancelAndConsumeRemainingEvents()
            errorUiState.cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `update counter with value, updated ui state`() {
        runTest {
            val notificationFlow = MutableSharedFlow<TopChatResult<NotificationsPojo>>()
            coEvery {
                getChatNotification.observe()
            } returns notificationFlow

            viewModel.chatListNotificationUiState.test {
                viewModel.setupViewModelObserver()
                skipItems(1)

                viewModel.processAction(
                    TopChatListAction.UpdateCounter(true, 1)
                )
                val updatedState1 = awaitItem()
                Assert.assertEquals(
                    1,
                    updatedState1.unreadSeller
                )

                viewModel.processAction(
                    TopChatListAction.UpdateCounter(false, 1)
                )
                val updatedState2 = awaitItem()
                Assert.assertEquals(
                    1,
                    updatedState2.unreadBuyer
                )

                viewModel.processAction(
                    TopChatListAction.UpdateCounter(true, -1)
                )
                val updatedState3 = awaitItem()
                Assert.assertEquals(
                    0,
                    updatedState3.unreadSeller
                )

                viewModel.processAction(
                    TopChatListAction.UpdateCounter(false, -1)
                )
                val updatedState4 = awaitItem()
                Assert.assertEquals(
                    0,
                    updatedState4.unreadBuyer
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `set last visited tab, cache updated`() {
        runTest {
            var expectedLastVisitedTab = -1
            val lastVisitedTabFlow = MutableSharedFlow<Int>()
            coEvery {
                getChatLastVisitedTab.observeLastVisitedTab()
            } returns lastVisitedTabFlow

            coEvery {
                getChatLastVisitedTab.setLastVisitedTab(any())
            } coAnswers {
                lastVisitedTabFlow.emit(expectedLastVisitedTab)
            }

            viewModel.chatLastSelectedTab.test {
                viewModel.setupViewModelObserver()
                skipItems(1) // Loading

                // When move to buyer tab
                expectedLastVisitedTab = 1
                viewModel.processAction(TopChatListAction.SetLastVisitedTab(expectedLastVisitedTab))

                // Then buyer tab
                Assert.assertEquals(
                    expectedLastVisitedTab,
                    (awaitItem() as TopChatResult.Success).data
                )

                // When move to seller tab
                expectedLastVisitedTab = 0
                viewModel.processAction(TopChatListAction.SetLastVisitedTab(expectedLastVisitedTab))

                // Then seller tab
                Assert.assertEquals(
                    expectedLastVisitedTab,
                    (awaitItem() as TopChatResult.Success).data
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe notification counter, get error`() {
        runTest {
            val expectedThrowable = Throwable("Oops!")
            every {
                getChatNotification.observe()
            } throws expectedThrowable

            viewModel.errorUiState.test {
                // When
                viewModel.setupViewModelObserver()

                // Then
                val errorState = awaitItem()
                Assert.assertEquals(
                    "Oops!",
                    errorState.error?.first?.message
                )
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `set last visited tab, get error`() {
        runTest {
            val expectedThrowable = Throwable("Oops!")
            coEvery {
                getChatLastVisitedTab.setLastVisitedTab(any())
            } throws expectedThrowable

            viewModel.errorUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TopChatListAction.SetLastVisitedTab(1))

                // Then
                val errorState = awaitItem()
                Assert.assertEquals(
                    "Oops!",
                    errorState.error?.first?.message
                )
                cancelAndConsumeRemainingEvents()
            }
        }
    }
}

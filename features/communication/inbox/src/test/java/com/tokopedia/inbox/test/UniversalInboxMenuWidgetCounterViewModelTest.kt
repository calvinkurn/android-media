package com.tokopedia.inbox.test

import app.cash.turbine.test
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxAllCounterResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxMenuDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWidgetDataResponse
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.GOJEK_TYPE
import com.tokopedia.inbox.universalinbox.view.UniversalInboxAction
import com.tokopedia.inbox.universalinbox.view.uimodel.MenuItemType
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class UniversalInboxMenuWidgetCounterViewModelTest : UniversalInboxViewModelTestFixture() {

    @Test
    fun `observe inbox menu empty cache, call inbox menu fallback`() {
        runTest {
            // Given
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))

            mockWidgetMeta(Result.Success(null))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            // When
            viewModel.setupViewModelObserver()
            viewModel.processAction(UniversalInboxAction.RefreshPage)

            // Then
            verify(exactly = 1) {
                inboxMenuMapper.generateFallbackMenu()
            }
            coVerify(exactly = 1) {
                getWidgetMetaUseCase.updateCache(any())
            }
        }
    }

    @Test
    fun `observe inbox menu cache and fetch, get inbox menu`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResponseCounter = UniversalInboxAllCounterResponse()
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))
            val expectedResultMenu = inboxMenuMapper.mapToInboxMenu(
                userSession,
                expectedResponse.chatInboxMenu.inboxMenu,
                expectedResponseCounter
            )
            val expectedResultWidget = inboxWidgetMetaMapper.mapWidgetMetaToUiModel(
                expectedResponse.chatInboxMenu.widgetMenu,
                expectedResponseCounter,
                Result.Success(expectedResponseDriver)
            )

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then empty state
                val initialState = awaitItem()
                assert(initialState.widgetMeta.widgetList.isEmpty())
                assert(initialState.menuList.isEmpty())

                // When refresh page
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(updatedState.widgetMeta.widgetList.isNotEmpty())
                Assert.assertEquals(
                    expectedResultWidget.widgetList,
                    updatedState.widgetMeta.widgetList
                )
                assert(updatedState.menuList.isNotEmpty())
                Assert.assertEquals(
                    expectedResultMenu,
                    updatedState.menuList
                )
                Assert.assertEquals(
                    expectedResponseDriver.first().unreadCount,
                    updatedState.widgetMeta.widgetList.first().counter
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu cache, waiting for the widget meta`() {
        runTest {
            // Given
            val expectedResponseCounter = UniversalInboxAllCounterResponse()

            mockWidgetMeta(Result.Loading)
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(listOf(mockk(relaxed = true))))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip initial state
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(updatedState.isLoading)
                assert(updatedState.widgetMeta.widgetList.isEmpty())
                assert(updatedState.menuList.isEmpty())
                assert(updatedState.miscList.isEmpty())

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu cache, not waiting for the counter or driver`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResult = inboxMenuMapper.mapToInboxMenu(
                userSession,
                expectedResponse.chatInboxMenu.inboxMenu,
                UniversalInboxAllCounterResponse()
            )

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Loading)
            mockDriverCounter(Result.Loading)

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                assert(updatedState.menuList.isNotEmpty())
                Assert.assertEquals(expectedResult, updatedState.menuList)
            }
        }
    }

    @Test
    fun `observe inbox menu and fail to fetch it, call inbox menu fallback`() {
        runTest {
            // Given
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))

            mockWidgetMeta(Result.Error(dummyThrowable))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                assert(updatedState.widgetMeta.isError)

                cancelAndConsumeRemainingEvents()
            }

            // Then
            verify(exactly = 1) {
                inboxMenuMapper.generateFallbackMenu()
            }
            coVerify(exactly = 1) {
                getWidgetMetaUseCase.updateCache(any())
            }
        }
    }

    @Test
    fun `observe inbox menu and only counter gql fail, get inbox menu without counter menu`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))
            val expectedResult = inboxMenuMapper.mapToInboxMenu(
                userSession,
                expectedResponse.chatInboxMenu.inboxMenu,
                expectedResponseCounter
            )
            val expectedResultWidget = inboxWidgetMetaMapper.mapWidgetMetaToUiModel(
                expectedResponse.chatInboxMenu.widgetMenu,
                expectedResponseCounter,
                Result.Success(expectedResponseDriver)
            )

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Error(dummyThrowable))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                assert(updatedState.widgetMeta.widgetList.isNotEmpty())
                assert(updatedState.menuList.isNotEmpty())
                Assert.assertEquals(
                    expectedResultWidget.widgetList,
                    updatedState.widgetMeta.widgetList
                )
                Assert.assertEquals(
                    0,
                    (updatedState.menuList.first() as UniversalInboxMenuUiModel).counter
                )
                Assert.assertNotEquals(expectedResult, updatedState.menuList)
                Assert.assertEquals(
                    expectedResultWidget.widgetList.first().counter,
                    updatedState.widgetMeta.widgetList.first().counter
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu and only driver fail, get inbox menu without counter driver`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Error(dummyThrowable))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                assert(updatedState.widgetMeta.widgetList.isNotEmpty())
                assert(updatedState.widgetMeta.widgetList.first().isError)
            }
        }
    }

    @Test
    fun `observe inbox menu and all counter fail, get inbox menu without all counter`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResult = inboxMenuMapper.mapToInboxMenu(
                userSession,
                expectedResponse.chatInboxMenu.inboxMenu,
                UniversalInboxAllCounterResponse()
            )

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Error(dummyThrowable))
            mockDriverCounter(Result.Error(dummyThrowable))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                Assert.assertEquals(expectedResult, updatedState.menuList)
            }
        }
    }

    @Test
    fun `observe inbox menu and all fail, call inbox menu fallback without counter and show widget error`() {
        runTest {
            // Given
            mockWidgetMeta(Result.Error(dummyThrowable))
            mockCounter(Result.Error(dummyThrowable))
            mockDriverCounter(Result.Error(dummyThrowable))

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                assert(!updatedState.isLoading)
                assert(updatedState.widgetMeta.widgetList.isEmpty())
                assert(updatedState.widgetMeta.isError)
            }

            // Then
            verify(exactly = 1) {
                inboxMenuMapper.generateFallbackMenu()
            }
            coVerify(exactly = 1) {
                getWidgetMetaUseCase.updateCache(any())
            }
        }
    }

    @Test
    fun `on success get menu and counter but fail to map, call inbox menu fallback`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            every {
                inboxMenuMapper.mapToInboxMenu(any(), any(), any())
            } throws dummyThrowable

            viewModel.errorUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated state
                val updatedState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable,
                    updatedState.error?.first
                )
                Assert.assertEquals(
                    "onSuccessGetMenuAndCounter",
                    updatedState.error?.second
                )

                cancelAndConsumeRemainingEvents()
            }

            // Then
            verify(exactly = 1) {
                inboxMenuMapper.generateFallbackMenu()
            }
            coVerify(exactly = 1) {
                getWidgetMetaUseCase.updateCache(any())
            }
        }
    }

    @Test
    fun `observe inbox menu and refresh counter, get updated counter`() {
        runTest {
            // Given
            val expectedResponse = UniversalInboxWrapperResponse().also {
                it.chatInboxMenu.widgetMenu = listOf(
                    UniversalInboxWidgetDataResponse(
                        title = "1",
                        type = GOJEK_TYPE
                    )
                )
                it.chatInboxMenu.inboxMenu = listOf(
                    UniversalInboxMenuDataResponse(
                        title = "2",
                        type = MenuItemType.CHAT_BUYER.counterType
                    )
                )
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))

            mockWidgetMeta(Result.Success(expectedResponse))
            mockDriverCounter(Result.Success(expectedResponseDriver))

            var counterResponse = UniversalInboxAllCounterResponse()
            val counterFlow = MutableSharedFlow<Result<UniversalInboxAllCounterResponse>>()
            coEvery {
                getAllCounterUseCase.observe()
            } returns counterFlow

            coEvery {
                getAllCounterUseCase.refreshCounter(any())
            } coAnswers {
                counterFlow.emit(Result.Success(counterResponse))
            }

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                val updatedState = awaitItem()
                Assert.assertEquals(
                    0,
                    (updatedState.menuList.first() as UniversalInboxMenuUiModel).counter
                )

                counterResponse = UniversalInboxAllCounterResponse().apply {
                    this.chatUnread.unreadBuyer = 1
                }
                counterFlow.emit(Result.Success(counterResponse))
                // When update state 2
                viewModel.processAction(UniversalInboxAction.RefreshCounter)
                // Then updated state 2
                val newState = awaitItem()
                Assert.assertEquals(
                    1,
                    (newState.menuList.first() as UniversalInboxMenuUiModel).counter
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu and fail fetch inbox, get fallback menu and error message`() {
        runTest {
            // Given
            val expectedResponse = inboxMenuMapper.generateFallbackMenu()
            val expectedResponseCounter = UniversalInboxAllCounterResponse().also {
                it.chatUnread.unreadBuyer = 1
                it.notifCenterUnread.notifUnread = "3"
            }
            val expectedResponseDriver = listOf(getDummyConversationsChannel(unreadCount = 1))
            val expectedResult = inboxMenuMapper.mapToInboxMenu(
                userSession,
                expectedResponse.chatInboxMenu.inboxMenu,
                expectedResponseCounter
            )

            mockWidgetMeta(Result.Success(expectedResponse))
            mockCounter(Result.Success(expectedResponseCounter))
            mockDriverCounter(Result.Success(expectedResponseDriver))
            coEvery {
                getWidgetMetaUseCase.fetchInboxMenuAndWidgetMeta()
            } throws dummyThrowable

            viewModel.inboxMenuUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()
                // Then skip
                skipItems(1)

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                val updatedState = awaitItem()
                Assert.assertEquals(
                    expectedResult,
                    updatedState.menuList
                )

                cancelAndConsumeRemainingEvents()
            }
            viewModel.errorUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)
                // Then updated error state
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "loadInboxMenuAndWidgetMeta",
                    errorState.error?.second
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox and fail to generate fallback, get error message`() {
        runTest {
            // Given
            mockWidgetMeta(Result.Success(null))
            mockCounter(Result.Loading)
            mockDriverCounter(Result.Success(listOf()))
            every {
                inboxMenuMapper.generateFallbackMenu()
            } throws dummyThrowable

            viewModel.errorUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshPage)

                // Then updated error state
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "setFallbackInboxMenu",
                    errorState.error?.second
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu and fail to refresh counter, get error message`() {
        runTest {
            // Given
            coEvery {
                getAllCounterUseCase.refreshCounter(any())
            } throws dummyThrowable

            viewModel.errorUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When update state
                viewModel.processAction(UniversalInboxAction.RefreshCounter)

                // Then updated error state
                val errorState = awaitItem()
                Assert.assertEquals(
                    dummyThrowable.message,
                    errorState.error?.first?.message
                )
                Assert.assertEquals(
                    "refreshCounter",
                    errorState.error?.second
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe inbox menu and refresh driver, get new job`() {
        runTest {
            // When
            viewModel.setupViewModelObserver()
            val oldJob = viewModel.driverJob
            viewModel.processAction(UniversalInboxAction.RefreshDriverWidget)

            // Then
            Assert.assertNotEquals(
                oldJob,
                viewModel.driverJob
            )
        }
    }

    private fun mockWidgetMeta(expectedResult: Result<UniversalInboxWrapperResponse?>) {
        coEvery {
            getWidgetMetaUseCase.observe()
        } returns flow {
            emit(expectedResult)
        }
    }

    private fun mockCounter(expectedResult: Result<UniversalInboxAllCounterResponse>) {
        coEvery {
            getAllCounterUseCase.observe()
        } returns flow {
            emit(expectedResult)
        }
    }

    private fun mockDriverCounter(expectedResult: Result<List<ConversationsChannel>>) {
        coEvery {
            getDriverChatCounterUseCase.observe()
        } returns flow {
            emit(expectedResult)
        }
    }
}

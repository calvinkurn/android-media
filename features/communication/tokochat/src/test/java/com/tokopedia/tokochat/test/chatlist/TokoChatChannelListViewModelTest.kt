package com.tokopedia.tokochat.test.chatlist

import app.cash.turbine.test
import com.gojek.conversations.babble.network.data.ChannelMetaData
import com.gojek.conversations.babble.network.data.OrderInfo
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.tokochat.base.TokoChatListViewModelTestFixture
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.util.TokoChatValueUtil
import com.tokopedia.tokochat.view.chatlist.TokoChatListAction
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoChatChannelListViewModelTest : TokoChatListViewModelTestFixture() {

    @Test
    fun `refresh page, get chat list data`() {
        runTest {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(dummyChannelList)) // Mock data from fetch local
                emit(TokoChatResult.Success(dummyChannelList)) // Mock data from fetch remote
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                // Then
                val initialValue = awaitItem()
                assertEquals(initialValue.page, 0)
                assertEquals(initialValue.chatItemList.size, 0)
                assertEquals(initialValue.errorMessage, null)
                assertEquals(initialValue.hasNextPage, true)
                assertEquals(initialValue.isLoading, false)

                val localLoadingValue = awaitItem()
                assertEquals(localLoadingValue.page, 0)
                assertEquals(localLoadingValue.chatItemList.size, 0)
                assertEquals(localLoadingValue.errorMessage, null)
                assertEquals(localLoadingValue.hasNextPage, true)
                assertEquals(localLoadingValue.isLoading, true)

                val updatedValue = awaitItem()
                assertEquals(updatedValue.page, 1)
                assertEquals(updatedValue.chatItemList.size, 1)
                assertEquals(updatedValue.errorMessage, null)
                assertEquals(updatedValue.hasNextPage, true)
                assertEquals(updatedValue.isLoading, false)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `refresh page, get empty chat list data`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(listOf()))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(listOf()))
            }

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                skipItems(3) // initial, loading, page increment

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.page, 1)
                assertEquals(updatedValue.chatItemList.size, 0)
                assertEquals(updatedValue.errorMessage, null)
                assertEquals(updatedValue.hasNextPage, false)
                assertEquals(updatedValue.isLoading, false)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `refresh page, get large chat list data`() {
        runTest {
            // Given
            val largeChatData: ArrayList<ConversationsChannel> = arrayListOf()
            for (i in 0..100) {
                largeChatData += getDummyConversationChannelList()
            }
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(largeChatData)) // Mock data from fetch local
                emit(TokoChatResult.Success(largeChatData)) // Mock data from fetch remote
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                skipItems(2) // initial & loading

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.chatItemList.size, (largeChatData.size / 2)) // half of it expired

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe and collect local channels, get error`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Error(throwableDummy))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow { }

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                skipItems(2) // initial & loading

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.errorMessage, throwableDummy.message)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe and collect remote channels, get error`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(listOf()))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow {
                emit(TokoChatResult.Error(throwableDummy))
            }

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                skipItems(3) // initial, loading, & empty list

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.errorMessage, throwableDummy.message)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe and collect channels, get error`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } throws throwableDummy

            viewModel.errorUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.error?.first, throwableDummy)
                assertEquals(updatedValue.error?.second, "observeChatListItemFlow")
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `observe and collect channels, but previous job is not null`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flowOf()

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)
                viewModel.processAction(TokoChatListAction.RefreshPage)

                verify(exactly = 2) {
                    chatListUseCase.fetchAllCachedChannels(any())
                }

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `load next page, get chat list data tracker`() {
        runTest {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow { }

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(dummyChannelList))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            viewModel.chatListTrackerUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)
                viewModel.processAction(TokoChatListAction.LoadNextPage)

                // Then
                val updatedValue = awaitItem()
                assertEquals(1, updatedValue.size)
                assertEquals(1, updatedValue["tokofood"])

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `load next page not first page, get not more than one chat list data tracker`() {
        runTest {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow { }

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(dummyChannelList))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            viewModel.chatListTrackerUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage)
                viewModel.processAction(TokoChatListAction.LoadNextPage)
                viewModel.processAction(TokoChatListAction.LoadNextPage)

                // Then
                verify(exactly = 2) {
                    chatListUseCase.fetchAllRemoteChannels(any(), any())
                }
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `load next page, get chat list data and page increased`() {
        runTest {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatListUseCase.fetchAllCachedChannels(
                    channelTypes = any()
                )
            } returns flow {
                emit(TokoChatResult.Loading)
                delay(10)
                emit(TokoChatResult.Success(dummyChannelList))
            }

            every {
                abTestPlatform.getString(any(), any())
            } returns TokoChatValueUtil.ROLLENCE_LOGISTIC_CHAT

            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } returns flow { }

            viewModel.chatListUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.RefreshPage) // 1st page
                viewModel.processAction(TokoChatListAction.LoadNextPage) // 2nd page
                viewModel.processAction(TokoChatListAction.LoadNextPage) // 3rd page
                viewModel.processAction(TokoChatListAction.LoadNextPage) // 4th page

                skipItems(2) // initial & loading

                val firstPageValue = awaitItem()
                val secondPageValue = awaitItem()
                val thirdPageValue = awaitItem()
                val forthPageValue = awaitItem()

                assertEquals(firstPageValue.page, 1)
                assertEquals(secondPageValue.page, 2)
                assertEquals(thirdPageValue.page, 3)
                assertEquals(forthPageValue.page, 4)

                // Then
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `load next page, get error`() {
        runTest {
            // Given
            every {
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = any(),
                    batchSize = any()
                )
            } throws throwableDummy

            viewModel.errorUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.LoadNextPage)

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.error?.first, throwableDummy)
                assertEquals(updatedValue.error?.second, "loadNextPageChatList")
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `navigate with applink, go to page with applink`() {
        runTest {
            // Given
            val dummyApplink = "applink"
            viewModel.navigationUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(TokoChatListAction.NavigateToPage(dummyApplink))

                // Then
                val updatedValue = awaitItem()
                assertEquals(updatedValue.applink, dummyApplink)
            }
        }
    }

    private fun getDummyConversationChannelList(): List<ConversationsChannel> {
        return listOf(
            ConversationsChannel(
                "", "", "", "dummyName", "", 0,
                null, null, members = listOf(), false,
                0L, expiresAt = Long.MAX_VALUE, 0L,
                lastRead = mapOf(), 0L,
                metadata = ChannelMetaData(
                    orderInfo = OrderInfo(
                        5, "", "", "", "",
                        null, null, null, null
                    ),
                    null,
                    null
                )
            ),
            ConversationsChannel(
                "", "", "", "dummyName2", "", 0,
                null, null, members = listOf(), false,
                0L, expiresAt = 1L, 0L,
                lastRead = mapOf(), 0L,
                metadata = ChannelMetaData(
                    orderInfo = OrderInfo(
                        14, "", "", "", "",
                        null, null, null, null
                    ),
                    null,
                    null
                )
            )
        )
    }
}

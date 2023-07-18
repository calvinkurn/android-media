package com.tokopedia.tokochat.test.chatlist

import com.gojek.conversations.babble.network.data.ChannelMetaData
import com.gojek.conversations.babble.network.data.OrderInfo
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.base.TokoChatListViewModelTestFixture
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.TOKOFOOD
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.invoke
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatChannelListViewModelTest : TokoChatListViewModelTestFixture() {

    @Test
    fun `should return channel list when success get from DB`() {
        runBlocking {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } returns flowOf(dummyChannelList)
            every {
                mapper.mapToChatListItem(any())
            } returns TokoChatListItemUiModel(
                "dummyName",
                "", "", "", "",
                Long.MAX_VALUE, 0, 0, ""
            )

            // When
            var result: Result<List<TokoChatListItemUiModel>>? = null
            viewModel.getChatListFlow().collectLatest {
                result = it
            }

            // Then
            Assert.assertEquals(
                dummyChannelList.first().name,
                (result as Success).data.first().orderId
            )
        }
    }

    @Test
    fun `should return empty channel list when success get from empty DB`() {
        runBlocking {
            // Given
            val dummyChannelList: List<ConversationsChannel> = listOf()
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } returns flowOf(dummyChannelList)

            // When
            var result: Result<List<TokoChatListItemUiModel>>? = null
            viewModel.getChatListFlow().collectLatest {
                result = it
            }

            // Then
            Assert.assertEquals(
                dummyChannelList.firstOrNull(),
                (result as Success).data.firstOrNull()
            )
        }
    }

    @Test
    fun `should throw error when fail to get data from DB`() {
        runBlocking {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } returns flowOf(dummyChannelList)
            every {
                mapper.mapToChatListItem(any())
            } throws throwableDummy

            // When
            var result: Result<List<TokoChatListItemUiModel>>? = null
            viewModel.getChatListFlow().collectLatest {
                result = it
            }

            // Then
            Assert.assertEquals(
                throwableDummy.message,
                (result as Fail).throwable.message
            )
        }
    }

    @Test
    fun `should throw error when fail to map data from DB`() {
        runBlocking {
            // Given
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } throws throwableDummy

            // When
            var result: Result<List<TokoChatListItemUiModel>>? = null
            viewModel.getChatListFlow().collectLatest {
                result = it
            }

            // Then
            Assert.assertEquals(
                throwableDummy.message,
                (result as Fail).throwable.message
            )
        }
    }

    @Test
    fun `should return channel list when success get from remote`() {
        runBlocking {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            val result = mapOf(Pair(TOKOFOOD, 0))
            every {
                chatChannelUseCase.getAllChannel(any(), any(), captureLambda(), any())
            } answers {
                val onSuccessDummy = lambda<(List<ConversationsChannel>) -> Unit>()
                onSuccessDummy.invoke(dummyChannelList)
            }
            every {
                mapper.mapToTypeCounter(any())
            } returns result

            // When
            viewModel.loadNextPageChatList()

            // Then
            Assert.assertEquals(
                result,
                viewModel.chatListPair.value
            )
        }
    }

    @Test
    fun `should return channel list when success get from remote but empty`() {
        runBlocking {
            // Given
            val result = mapOf<String, Int>()
            every {
                chatChannelUseCase.getAllChannel(any(), any(), captureLambda(), any())
            } answers {
                val onSuccessDummy = lambda<(List<ConversationsChannel>) -> Unit>()
                onSuccessDummy.invoke(listOf())
            }
            every {
                mapper.mapToTypeCounter(any())
            } returns result

            // When
            viewModel.loadNextPageChatList()

            // Then
            Assert.assertEquals(
                result,
                viewModel.chatListPair.value
            )
        }
    }

    @Test
    fun `should return channel list when success get from remote with big local size`() {
        runBlocking {
            // Given
            val dummyChannelList = getDummyConversationChannelList()
            val result = mapOf(Pair(TOKOFOOD, 0))
            every {
                chatChannelUseCase.getAllChannel(any(), any(), captureLambda(), any())
            } answers {
                val onSuccessDummy = lambda<(List<ConversationsChannel>) -> Unit>()
                onSuccessDummy.invoke(dummyChannelList)
            }
            every {
                mapper.mapToTypeCounter(any())
            } returns result

            // When
            viewModel.loadNextPageChatList(localSize = 100)

            // Then
            Assert.assertEquals(
                result,
                viewModel.chatListPair.value
            )
        }
    }

    @Test
    fun `should throw error when fail get from remote`() {
        runBlocking {
            // Given
            val dummyError = ConversationsNetworkError(throwableDummy)
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } returns flowOf(listOf())
            every {
                chatChannelUseCase.getAllChannel(any(), any(), any(), captureLambda())
            } answers {
                val onErrorDummy = lambda<(ConversationsNetworkError?) -> Unit>()
                onErrorDummy.invoke(dummyError)
            }

            // When
            viewModel.loadNextPageChatList()

            // Then
            Assert.assertEquals(
                dummyError.message,
                viewModel.error.value?.first?.message
            )
        }
    }

    @Test
    fun `should throw error when fail get from remote but no error message`() {
        runBlocking {
            // Given
            every {
                chatChannelUseCase.getAllCachedChannels(any())
            } returns flowOf(listOf())
            every {
                chatChannelUseCase.getAllChannel(any(), any(), any(), captureLambda())
            } answers {
                val onErrorDummy = lambda<(ConversationsNetworkError?) -> Unit>()
                onErrorDummy.invoke(null)
            }

            // When
            viewModel.loadNextPageChatList()

            // Then
            Assert.assertEquals(
                null,
                viewModel.error.value?.first?.message
            )
        }
    }

    @Test
    fun `should throw error when error calling SDK method`() {
        runBlocking {
            // Given
            every {
                chatChannelUseCase.getAllChannel(any(), any(), any(), captureLambda())
            } throws throwableDummy

            // When
            viewModel.loadNextPageChatList()

            // Then
            Assert.assertEquals(
                throwableDummy.message,
                viewModel.error.value?.first?.message
            )
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
                    null, null
                )
            ),
            ConversationsChannel(
                "", "", "", "dummyName2", "", 0,
                null, null, members = listOf(), false,
                0L, expiresAt = 0L, 0L,
                lastRead = mapOf(), 0L,
                metadata = ChannelMetaData(
                    orderInfo = OrderInfo(
                        0, "", "", "", "",
                        null, null, null, null
                    ),
                    null, null
                )
            )
        )
    }
}

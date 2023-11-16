package com.tokopedia.tokochat.test.chatroom

import androidx.lifecycle.MutableLiveData
import app.cash.turbine.test
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.SOURCE_TOKOFOOD
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.invoke
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoChatChannelViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `initGroupBooking, get channel id`() {
        runTest {
            // Given
            every {
                tokoChatGroupBookingUseCase.initGroupBookingChat(any(), any(), any())
            } returns flow {
                emit(TokoChatResult.Loading)
                emit(TokoChatResult.Success(CHANNEL_ID_DUMMY))
            }

            viewModel.groupBookingUiState.test {
                // When
                viewModel.initGroupBooking()

                // Then
                val updatedValue = awaitItem()
                assertEquals(CHANNEL_ID_DUMMY, updatedValue.channelUrl)
                assertEquals(null, updatedValue.error)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `initGroupBooking, get error result`() {
        runTest {
            // Given
            every {
                tokoChatGroupBookingUseCase.initGroupBookingChat(any(), any(), any())
            } returns flow {
                emit(TokoChatResult.Loading)
                emit(TokoChatResult.Error(throwableDummy))
            }

            viewModel.groupBookingUiState.test {
                // When
                viewModel.initGroupBooking()

                // Then
                val updatedValue = awaitItem()
                assertEquals("", updatedValue.channelUrl)
                assertEquals(throwableDummy, updatedValue.error)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `initGroupBooking, get error throwable`() {
        runTest {
            // Given
            every {
                tokoChatGroupBookingUseCase.initGroupBookingChat(any(), any(), any())
            } throws throwableDummy

            viewModel.groupBookingUiState.test {
                // When
                viewModel.gojekOrderId = GOJEK_ORDER_ID_DUMMY
                viewModel.source = SOURCE_TOKOFOOD
                viewModel.initGroupBooking()

                // Then
                verify(exactly = 1) {
                    tokoChatGroupBookingUseCase.initGroupBookingChat(
                        GOJEK_ORDER_ID_DUMMY,
                        tokoChatGroupBookingUseCase.getServiceType(SOURCE_TOKOFOOD),
                        OrderChatType.Driver
                    )
                }

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when successfully getGroupBookingChannel should give GroupBookingChannelDetails`() {
        runTest {
            // Given
            val channelDummy = mockk<GroupBookingChannelDetails>(relaxed = true)
            coEvery {
                tokoChatRoomUseCase.getRemoteGroupBookingChannel(any(), captureLambda(), any())
            } answers {
                val onSuccess = lambda<(channel: GroupBookingChannelDetails) -> Unit>()
                onSuccess.invoke(channelDummy)
            }

            // When
            viewModel.getGroupBookingChannel(CHANNEL_ID_DUMMY)
            val result = (viewModel.channelDetail.observeAwaitValue() as Success).data

            // Then
            Assert.assertEquals(channelDummy, result)
        }
    }

    @Test
    fun `when error while getGroupBookingChannel should give throwable in channelDetail livedata`() {
        runTest {
            // Given
            val conversationsNetworkErrorDummy = mockk<ConversationsNetworkError>(relaxed = true)
            coEvery {
                tokoChatRoomUseCase.getRemoteGroupBookingChannel(any(), any(), captureLambda())
            } answers {
                val onError = lambda<(error: ConversationsNetworkError) -> Unit>()
                onError.invoke(conversationsNetworkErrorDummy)
            }

            // When
            viewModel.getGroupBookingChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                conversationsNetworkErrorDummy,
                (viewModel.channelDetail.observeAwaitValue() as Fail).throwable
            )
        }
    }

    @Test
    fun `when failed to getGroupBookingChannel should give throwable in error livedata`() {
        runTest {
            val channelDummy = mockk<GroupBookingChannelDetails>(relaxed = true)
            // Given
            coEvery {
                tokoChatRoomUseCase.getRemoteGroupBookingChannel(any(), captureLambda(), any())
            } throws throwableDummy

            // When
            viewModel.getGroupBookingChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `when getLiveChannel should give ConversationsChannel`() {
        runTest {
            // Given
            val conversationChannelDummy = mockk<ConversationsChannel>(relaxed = true)
            coEvery {
                tokoChatRoomUseCase.getLiveChannel(any())
            } returns MutableLiveData(conversationChannelDummy)

            // When
            val result = viewModel.getLiveChannel(CHANNEL_ID_DUMMY)?.observeAwaitValue()

            // Then
            Assert.assertEquals(conversationChannelDummy, result)
        }
    }

    @Test
    fun `when failed to getLiveChannel should give throwable on error livedata`() {
        runTest {
            // Given
            coEvery {
                tokoChatRoomUseCase.getLiveChannel(any())
            } throws throwableDummy

            // When
            viewModel.getLiveChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()?.first
            )
        }
    }

    @Test
    fun `test gojekOrderId in viewmodel`() {
        val testGojekOrderId = "test gojek order id"
        viewModel.gojekOrderId = testGojekOrderId
        Assert.assertEquals(testGojekOrderId, viewModel.gojekOrderId)
    }

    @Test
    fun `test source in viewmodel`() {
        val testSource = "test source"
        viewModel.source = testSource
        Assert.assertEquals(testSource, viewModel.source)
    }

    @Test
    fun `test tkpd order id viewmodel`() {
        val testTkpdOrderId = "test tkpd order id"
        viewModel.tkpdOrderId = testTkpdOrderId
        Assert.assertEquals(testTkpdOrderId, viewModel.tkpdOrderId)
    }

    @Test
    fun `test channel id viewmodel`() {
        val testChannelId = "test channel id"
        viewModel.channelId = testChannelId
        Assert.assertEquals(testChannelId, viewModel.channelId)
    }

    @Test
    fun `test isFromTokoFoodPost viewmodel`() {
        val testIsFromTokoFoodPostPurchase = true
        viewModel.isFromTokoFoodPostPurchase = testIsFromTokoFoodPostPurchase
        Assert.assertEquals(testIsFromTokoFoodPostPurchase, viewModel.isFromTokoFoodPostPurchase)
    }

    @Test
    fun `test `() {
        val testPushNotifTemplateKey = "test_template"
        viewModel.pushNotifTemplateKey = testPushNotifTemplateKey
        Assert.assertEquals(testPushNotifTemplateKey, viewModel.pushNotifTemplateKey)
    }
}

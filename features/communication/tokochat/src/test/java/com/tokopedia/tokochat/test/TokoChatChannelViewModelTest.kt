package com.tokopedia.tokochat.test

import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.invoke
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class TokoChatChannelViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when initialization group booking should be called exactly once`() {
        runBlocking {
            // Given
            val dummyListener = object : ConversationsGroupBookingListener {
                override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {}
                override fun onGroupBookingChannelCreationStarted() {}
                override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {}
            }

            coEvery {
                getChannelUseCase.initGroupBookingChat(any(), any(), any(), any())
            } returns Unit

            // When
            viewModel.initGroupBooking(
                orderId = GOJEK_ORDER_ID_DUMMY,
                groupBookingListener = dummyListener
            )

            // Then
            coVerify(exactly = 1) {
                getChannelUseCase.initGroupBookingChat(GOJEK_ORDER_ID_DUMMY, any(), dummyListener, any())
            }
        }
    }

    @Test
    fun `when failed to initialization group booking should give throwable in error livedata`() {
        runBlocking {
            // Given
            val dummyListener = object : ConversationsGroupBookingListener {
                override fun onGroupBookingChannelCreationError(error: ConversationsNetworkError) {}
                override fun onGroupBookingChannelCreationStarted() {}
                override fun onGroupBookingChannelCreationSuccess(channelUrl: String) {}
            }

            coEvery {
                getChannelUseCase.initGroupBookingChat(any(), any(), any(), any())
            } throws throwableDummy

            // When
            viewModel.initGroupBooking(
                orderId = GOJEK_ORDER_ID_DUMMY,
                groupBookingListener = dummyListener
            )

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()
            )
        }
    }

    @Test
    fun `when successfully getGroupBookingChannel should give GroupBookingChannelDetails`() {
        runBlocking {
            // Given
            val channelDummy = mockk<GroupBookingChannelDetails>(relaxed = true)
            coEvery {
                getChannelUseCase.getRemoteGroupBookingChannel(any(), captureLambda(), any())
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
        runBlocking {
            // Given
            val conversationsNetworkErrorDummy = mockk<ConversationsNetworkError>(relaxed = true)
            coEvery {
                getChannelUseCase.getRemoteGroupBookingChannel(any(), any(), captureLambda())
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
        runBlocking {
            val channelDummy = mockk<GroupBookingChannelDetails>(relaxed = true)
            // Given
            coEvery {
                getChannelUseCase.getRemoteGroupBookingChannel(any(), captureLambda(), any())
            } throws throwableDummy

            // When
            viewModel.getGroupBookingChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()
            )
        }
    }

    @Test
    fun `when getLiveChannel should give ConversationsChannel`() {
        runBlocking {
            // Given
            val conversationChannelDummy = mockk<ConversationsChannel>(relaxed = true)
            coEvery {
                getChannelUseCase.getLiveChannel(any())
            } returns MutableLiveData(conversationChannelDummy)

            // When
            val result = viewModel.getLiveChannel(CHANNEL_ID_DUMMY).observeAwaitValue()

            // Then
            Assert.assertEquals(conversationChannelDummy, result)
        }
    }

    @Test
    fun `when failed to getLiveChannel should give throwable on error livedata`() {
        runBlocking {
            // Given
            coEvery {
                getChannelUseCase.getLiveChannel(any())
            } throws throwableDummy

            // When
            viewModel.getLiveChannel(CHANNEL_ID_DUMMY)

            // Then
            Assert.assertEquals(
                throwableDummy,
                viewModel.error.observeAwaitValue()
            )
        }
    }
}

package com.tokopedia.tokochat.test

import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.database.chats.ConversationsMessage
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import com.tokopedia.tokochat.domain.response.orderprogress.TokoChatOrderProgressResponse
import com.tokopedia.tokochat.domain.response.orderprogress.param.TokoChatOrderProgressParam
import com.tokopedia.tokochat.domain.response.ticker.TokochatRoomTickerResponse
import com.tokopedia.tokochat.utils.JsonResourcesUtil
import com.tokopedia.tokochat.utils.observeAwaitValue
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil
import com.tokopedia.tokochat_common.util.TokoChatValueUtil
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.invoke
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.io.File

class TokoChatViewModelTest : TokoChatViewModelTestFixture() {

    @Test
    fun `when getTokoChatBackground, this method should return livedata success`() {
        runBlocking {
            val expectedImageUrl = TokoChatUrlUtil.IC_TOKOFOOD_SOURCE

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } returns flowOf(expectedImageUrl)

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Success).data

            assertEquals(expectedImageUrl, actualResult)
        }
    }

    @Test
    fun `when getTokoChatBackground, this method should return livedata fail`() {
        runBlocking {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatBackgroundUseCase(Unit)
            } throws errorException

            // when
            viewModel.getTokoChatBackground()

            // then
            coVerify {
                getTokoChatBackgroundUseCase(Unit)
            }

            val actualResult = (viewModel.chatBackground.observeAwaitValue() as Fail).throwable::class.java

            val expectedResult = errorException::class.java
            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata success`() {
        runBlocking {
            val expectedTicker = TokochatRoomTickerResponse(
                tokochatRoomTicker = TokochatRoomTickerResponse.TokochatRoomTicker(
                    enable = true,
                    message = "Hi",
                    tickerType = 1
                )
            )

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            } returns expectedTicker

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Success).data

            assertEquals(expectedTicker, actualResult)
        }
    }

    @Test
    fun `when loadChatRoomTicker, this method should return livedata fail`() {
        runBlocking {
            val errorException = Throwable()

            // given
            coEvery {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            } throws errorException

            // when
            viewModel.loadChatRoomTicker()

            // then
            coVerify {
                getTokoChatRoomTickerUseCase(TokoChatValueUtil.TOKOFOOD)
            }

            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Fail).throwable::class.java
            val expectedResult = errorException::class.java

            assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should return set live data success`() {
        runBlocking {
            val tokoChatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_SUCCESS
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } returns tokoChatOrderProgressResponse

            viewModel.loadOrderCompletedStatus(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Success).data
            assertEquals(tokoChatOrderProgressResponse, actualResult)
        }
    }

    @Test
    fun `when fetchOrderDetail should set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } throws errorException

            viewModel.loadOrderCompletedStatus(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            val actualResult = (viewModel.orderTransactionStatus.value as Fail).throwable::class.java
            val expectedResult = errorException::class.java
            TestCase.assertEquals(expectedResult, actualResult)
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data success`() {
        runBlocking {
            val tokochatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_OTW_DESTINATION
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } returns tokochatOrderProgressResponse

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatValueUtil.TOKOFOOD)
            delay(5000L)

            val actualResult = (result.await() as Success).data

            assertEquals(tokochatOrderProgressResponse, actualResult)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            result.cancel()
        }
    }

    @Test
    fun `when updateOrderStatusParam is still in progress should return set live data error`() {
        runBlocking {
            val errorException = MessageErrorException()

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            } throws errorException

            val result = async {
                viewModel.updateOrderTransactionStatus.first()
            }

            viewModel.updateOrderStatusParam(TKPD_ORDER_ID_DUMMY to TokoChatValueUtil.TOKOFOOD)
            delay(5000L)

            val actualResult = result.await() as Fail
            TestCase.assertEquals(errorException::class.java, actualResult.throwable::class.java)

            coVerify {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam(TKPD_ORDER_ID_DUMMY, TokoChatValueUtil.TOKOFOOD))
            }

            result.cancel()
        }
    }

    @Test
    fun `given orderId is empty and source is empty when updateOrderStatusParam should return set live data success`() {
        runBlocking {
            val tokochatOrderProgressResponse = JsonResourcesUtil.createSuccessResponse<TokoChatOrderProgressResponse>(
                ORDER_TRACKING_OTW_DESTINATION
            )

            coEvery {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            } returns tokochatOrderProgressResponse

            viewModel.updateOrderStatusParam("" to "")
            delay(5000L)

            coVerify(exactly = 0) {
                getTokoChatOrderProgressUseCase(TokoChatOrderProgressParam("", ""))
            }
        }
    }

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
    fun `when successfully getGroupBookingChannel should return GroupBookingChannelDetails`() {
        runBlocking {
            val channelDummy = mockk<GroupBookingChannelDetails>(relaxed = true)
            // Given
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
            assertEquals(channelDummy, result)
        }
    }

    @Test
    fun `when getChatHistory should return List of ConversationsMessage`() {
        runBlocking {
            // Given
            val dummyListMessage = listOf<ConversationsMessage>()

            coEvery {
                getChatHistoryUseCase(any())
            } returns MutableLiveData(dummyListMessage)

            // When
            val result = viewModel.getChatHistory(CHANNEL_ID_DUMMY).observeAwaitValue()

            // Then
            assertEquals(dummyListMessage, result)
        }
    }

    @Test
    fun `when loadPreviousMessages should call loadPreviousMessage (from SDK) once`() {
        runBlocking {
            // When
            viewModel.loadPreviousMessages()

            // Then
            coVerify(exactly = 1) {
                getChatHistoryUseCase.loadPreviousMessage()
            }
        }
    }

    @Test
    fun `when markChatAsRead should call markAllMessagesAsRead (from SDK) once`() {
        runBlocking {
            // When
            viewModel.markChatAsRead(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                markAsReadUseCase(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when registerActiveChannel should call softRegisterChannel (from SDK) once`() {
        runBlocking {
            // When
            viewModel.registerActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.registerActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when deRegisterActiveChannel should call softDeregisterChannel (from SDK) once`() {
        runBlocking {
            // When
            viewModel.deRegisterActiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                registrationChannelUseCase.deRegisterActiveChannel(CHANNEL_ID_DUMMY)
            }
        }
    }

    @Test
    fun `when getTypingStatus should call getTypingStatusCallback (from SDK) once and return typing status`() {
        runBlocking {
            // Given
            val typingStatusListDummy = listOf("123", "456")
            coEvery {
                getTypingUseCase.getTypingStatus()
            } returns MutableLiveData(typingStatusListDummy)

            // When
            val result = viewModel.getTypingStatus().observeAwaitValue()

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.getTypingStatus()
            }
            assertEquals(typingStatusListDummy, result)
        }
    }

    @Test
    fun `when setTypingStatus should call setTypingStatus (from SDK) once`() {
        runBlocking {
            // Given
            val typingStatusDummy = true

            // When
            viewModel.setTypingStatus(typingStatusDummy)

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.setTypingStatus(typingStatusDummy)
            }
        }
    }

    @Test
    fun `when resetTypingStatus should call resetTypingStatusCallback (from SDK) once`() {
        runBlocking {
            // When
            viewModel.resetTypingStatus()

            // Then
            coVerify(exactly = 1) {
                getTypingUseCase.resetTypingStatus()
            }
        }
    }

    @Test
    fun `when send message should be called exactly once`() {
        runBlocking {
            // Given
            val dummyMessage = "message dummy 123"

            coEvery {
                sendMessageUseCase.sendTextMessage(any(), any(), any())
            } returns Unit

            // When
            viewModel.sendMessage(CHANNEL_ID_DUMMY, dummyMessage)

            // Then
            coVerify(exactly = 1) {
                sendMessageUseCase.sendTextMessage(CHANNEL_ID_DUMMY, dummyMessage, any())
            }
        }
    }

    @Test
    fun `when getUserId should give user id as non empty string`() {
        runBlocking {
            // Given
            coEvery {
                registrationChannelUseCase.getUserId()
            } returns USER_ID_DUMMY

            // When
            val result = viewModel.getUserId()

            // Then
            assertEquals(USER_ID_DUMMY, result)
        }
    }

    @Test
    fun `when getLiveChannel should give LiveData of channel`() {
        runBlocking {
            // Given
            val dummyLiveData = MutableLiveData<ConversationsChannel>()
            coEvery {
                getChannelUseCase.getLiveChannel(any())
            } returns dummyLiveData

            // When
            val result = viewModel.getLiveChannel(CHANNEL_ID_DUMMY)

            // Then
            coVerify(exactly = 1) {
                getChannelUseCase.getLiveChannel(CHANNEL_ID_DUMMY)
            }
            assertEquals(dummyLiveData, result)
        }
    }

    @Test
    fun `when doCheckChatConnection should set the connection check job`() {
        runBlocking {
            // Given
            val connectionDummy = true
            coEvery {
                getChannelUseCase.isChatConnected()
            } returns connectionDummy

            // When
            viewModel.doCheckChatConnection()

            val result = viewModel.isChatConnected.observeAwaitValue(time = 6000)

            // Then
            coVerify(exactly = 1) {
                viewModel.cancelCheckConnection()
            }

            coVerify {
                getChannelUseCase.isChatConnected()
            }

            assertEquals(connectionDummy, result)
            assertNotEquals(null, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when cancelCheckConnection should set connectionCheckJob as null`() {
        runBlocking {
            // Given
            val connectionDummy = true
            coEvery {
                getChannelUseCase.isChatConnected()
            } returns connectionDummy

            // When
            viewModel.doCheckChatConnection()
            viewModel.cancelCheckConnection()

            // Then
            assertEquals(null, viewModel.connectionCheckJob)
        }
    }

    @Test
    fun `when currentCoroutineContext on doCheckChatConnection is not active should not check chat connected`() {
        runBlocking {
            // When
            viewModel.doCheckChatConnection()
            viewModel.connectionCheckJob?.cancel()
            Thread.sleep(6000) // Wait until delay end

            // Then
            coVerify(exactly = 0) {
                getChannelUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `when currentCoroutineContext is inactive on doCheckChatConnection while looping should not check chat connected anymore`() {
        runBlocking {
            // When
            viewModel.doCheckChatConnection()
            Thread.sleep(6000) // Wait until delay end

            viewModel.connectionCheckJob?.cancel()
            Thread.sleep(6000) // Wait until delay end

            // Then
            coVerify(exactly = 1) {
                getChannelUseCase.isChatConnected()
            }
        }
    }

    @Test
    fun `set connection check job`() {
        runBlocking {
            // Given
            val dummyJob = mockk<Job>(relaxed = true)

            // When
            viewModel.connectionCheckJob = dummyJob

            // Then
            assertEquals(dummyJob, viewModel.connectionCheckJob)
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
            assertEquals(conversationChannelDummy, result)
        }
    }

    @Test
    fun `when getMemberLeft should give LiveData String of member left`() {
        runBlocking {
            // Given
            val memberLeftLiveDataDummy = MutableLiveData("123")
            coEvery {
                getChannelUseCase.getMemberLeftLiveData()
            } returns memberLeftLiveDataDummy

            // When
            val result = viewModel.getMemberLeft()

            // Then
            assertEquals(memberLeftLiveDataDummy, result)
        }
    }

    @Test
    fun `when getImageWithId with new Image, should save image and call onImageReady`() {
        runBlocking {
            // Given
            val imageIdDummy = "test123"
            val imageViewDummy = mockk<ImageView>(relaxed = true)
            val fileDummy = mockk<File>(relaxed = true)
            val imageResultDummy = TokoChatImageResult(success = true)
            val responseBodyDummy = mockk<ResponseBody>(relaxed = true)

            coEvery {
                getImageUrlUseCase(any())
            } returns imageResultDummy

            coEvery {
                getImageUrlUseCase.getImage(any())
            } returns responseBodyDummy

            coEvery {
                viewUtil.downloadAndSaveByteArrayImage(any(), any(), captureLambda(), any(), any(), any())
            } answers {
                val onSuccessDummy = lambda<(File?) -> Unit>()
                onSuccessDummy.invoke(fileDummy)
            }

            // When
            var result: File? = null
            viewModel.getImageWithId(
                imageId = imageIdDummy,
                channelId = CHANNEL_ID_DUMMY,
                onImageReady = {
                    result = it
                },
                onError = {},
                onDirectLoad = {},
                imageView = imageViewDummy,
                isFromRetry = false
            )
            Thread.sleep(2000)

            // Then
            assertEquals(fileDummy, result)
        }
    }
}

package com.tokopedia.tokochat.test.chatroom

import com.tokopedia.tokochat.base.TokoChatViewModelTestFixture

class TokoChatTickerViewModelTest : TokoChatViewModelTestFixture() {

//    @Test
//    fun `when loadChatRoomTicker, this method should return livedata success`() {
//        runBlocking {
//            val expectedTicker = TokochatRoomTickerResponse(
//                tokochatRoomTicker = TokochatRoomTickerResponse.TokochatRoomTicker(
//                    enable = true,
//                    message = "Hi",
//                    tickerType = 1
//                )
//            )
//
//            // given
//            coEvery {
//                getTokoChatRoomTickerUseCase(TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
//            } returns expectedTicker
//
//            // when
//            viewModel.loadChatRoomTicker()
//
//            // then
//            coVerify {
//                getTokoChatRoomTickerUseCase(TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
//            }
//
//            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Success).data
//
//            Assert.assertEquals(expectedTicker, actualResult)
//        }
//    }
//
//    @Test
//    fun `when loadChatRoomTicker, this method should return livedata fail`() {
//        runBlocking {
//            val errorException = Throwable()
//
//            // given
//            coEvery {
//                getTokoChatRoomTickerUseCase(TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
//            } throws errorException
//
//            // when
//            viewModel.loadChatRoomTicker()
//
//            // then
//            coVerify {
//                getTokoChatRoomTickerUseCase(TokoChatCommonValueUtil.SOURCE_TOKOFOOD)
//            }
//
//            val actualResult = (viewModel.chatRoomTicker.observeAwaitValue() as Fail).throwable::class.java
//            val expectedResult = errorException::class.java
//
//            Assert.assertEquals(expectedResult, actualResult)
//        }
//    }
}

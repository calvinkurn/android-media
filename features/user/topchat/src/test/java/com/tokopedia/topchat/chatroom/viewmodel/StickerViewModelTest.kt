package com.tokopedia.topchat.chatroom.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.ChatListStickerUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.StickerViewModel
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StickerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var chatListStickerUseCase: ChatListStickerUseCase

    @RelaxedMockK
    lateinit var cacheManager: TopchatCacheManager

    private lateinit var viewModel: StickerViewModel

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val testStickerUUID = "testUUID"
    private val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = StickerViewModel(chatListStickerUseCase, cacheManager, dispatchers)
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns null
        every {
            cacheManager.getPreviousState(any())
        } returns false

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult
        
        //When
        viewModel.loadStickers(testStickerUUID, false)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers_and_need_update() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns null
        every {
            cacheManager.getPreviousState(any())
        } returns false

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadStickers(testStickerUUID, true)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers_and_fail_to_get_cache() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } throws expectedThrowable
        every {
            cacheManager.getPreviousState(any())
        } throws expectedThrowable

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadStickers(testStickerUUID, false)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers_from_cache() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns expectedResult
        every {
            cacheManager.getPreviousState(any())
        } returns true

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadStickers(testStickerUUID, false)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers_from_cache_but_need_update() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns expectedResult
        every {
            cacheManager.getPreviousState(any())
        } returns true

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadStickers(testStickerUUID, true)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_sticker_list_when_successful_get_stickers_from_cache_but_previous_false() {
        //Given
        val expectedResult = getExpectedResponse()
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns expectedResult
        every {
            cacheManager.getPreviousState(any())
        } returns false

        coEvery {
            chatListStickerUseCase(any())
        } returns expectedResult

        //When
        viewModel.loadStickers(testStickerUUID, false)

        //Then
        Assert.assertEquals(
            expectedResult.chatBundleSticker.list.first().stickerUUID,
            (viewModel.stickers.value as Success).data.first().stickerUUID
        )
    }

    @Test
    fun should_get_exception_when_error_get_stickers_and_not_cached() {
        //Given
        every {
            cacheManager.loadCache<StickerResponse?>(any(), StickerResponse::class.java)
        } returns null
        every {
            cacheManager.getPreviousState(any())
        } returns false

        coEvery {
            chatListStickerUseCase(any())
        } throws expectedThrowable

        //When
        viewModel.loadStickers(testStickerUUID, false)

        //Then
        Assert.assertEquals(
            expectedThrowable.message,
            (viewModel.stickers.value as Fail).throwable.message
        )
    }

    private fun getExpectedResponse(): StickerResponse {
        return StickerResponse().apply {
            this.chatBundleSticker.list = arrayListOf(
                Sticker(stickerUUID = testStickerUUID)
            )
        }
    }
}
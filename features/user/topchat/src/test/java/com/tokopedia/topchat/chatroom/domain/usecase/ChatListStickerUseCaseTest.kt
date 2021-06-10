package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.TopchatCacheManagerStub
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatListStickerUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var gqlUseCase: GraphqlUseCase<StickerResponse>
    @RelaxedMockK
    private lateinit var onLoading: (List<Sticker>) -> Unit
    @RelaxedMockK
    private lateinit var onSuccess: (List<Sticker>) -> Unit
    @RelaxedMockK
    private lateinit var onError: (Throwable) -> Unit
    private lateinit var cacheManager: TopchatCacheManagerStub
    private val testDispatcher = CoroutineTestDispatchersProvider

    private lateinit var chatListStickerUseCase: ChatListStickerUseCase

    object Dummy {
        val stickerUUID = "c6a02920-9695-11ea-aa61-000000000003"
        val successResponse = FileUtil.parse<StickerResponse>(
                "/success_chat_bundle_sticker.json",
                StickerResponse::class.java
        )
        val cacheResponse = FileUtil.parse<StickerResponse>(
                "/success_chat_bundle_sticker.json",
                StickerResponse::class.java
        )
        val error = Throwable()
        var needUpdate = false
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        cacheManager = spyk(TopchatCacheManagerStub(Dummy.successResponse))
        chatListStickerUseCase = ChatListStickerUseCase(gqlUseCase, cacheManager, testDispatcher)
    }

    @Test
    fun `load sticker from cache`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        cacheManager.isPreviousLoadSuccess = true
        Dummy.needUpdate = false
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        chatListStickerUseCase.loadSticker(Dummy.stickerUUID, Dummy.needUpdate, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 0) { onLoading.invoke(Dummy.cacheResponse.chatBundleSticker.list) }
        coVerify(exactly = 0) { gqlUseCase.executeOnBackground() }
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.cacheResponse.chatBundleSticker.list) }
    }

    @Test
    fun `load sticker from network without cache`() = runBlocking {
        // Given
        cacheManager.cache = null
        cacheManager.isPreviousLoadSuccess = false
        Dummy.needUpdate = true
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        chatListStickerUseCase.loadSticker(Dummy.stickerUUID, Dummy.needUpdate, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 0) { onLoading.invoke(any()) }
        coVerify(exactly = 1) { gqlUseCase.executeOnBackground() }
        coVerify(exactly = 1) { cacheManager.saveCache("ChatListStickerUseCase - c6a02920-9695-11ea-aa61-000000000003", Dummy.successResponse) }
        coVerify(exactly = 1) { cacheManager.saveState("state - c6a02920-9695-11ea-aa61-000000000003", true) }
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.successResponse.chatBundleSticker.list) }
    }

    @Test
    fun `load sticker from network with cache`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        cacheManager.isPreviousLoadSuccess = false
        Dummy.needUpdate = true
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        chatListStickerUseCase.loadSticker(Dummy.stickerUUID, Dummy.needUpdate, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { onLoading.invoke(Dummy.cacheResponse.chatBundleSticker.list) }
        coVerify(exactly = 1) { gqlUseCase.executeOnBackground() }
        coVerify(exactly = 1) { cacheManager.saveCache("ChatListStickerUseCase - c6a02920-9695-11ea-aa61-000000000003", Dummy.successResponse) }
        coVerify(exactly = 1) { cacheManager.saveState("state - c6a02920-9695-11ea-aa61-000000000003", true) }
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.successResponse.chatBundleSticker.list) }
    }


    @Test
    fun `load sticker failed from network`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        cacheManager.isPreviousLoadSuccess = false
        Dummy.needUpdate = true
        coEvery { gqlUseCase.executeOnBackground() } throws Dummy.error
        // When
        chatListStickerUseCase.loadSticker(Dummy.stickerUUID, Dummy.needUpdate, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { onLoading.invoke(Dummy.cacheResponse.chatBundleSticker.list) }
        coVerify(exactly = 1) { gqlUseCase.executeOnBackground() }
        coVerify(exactly = 0) { cacheManager.saveCache("ChatListStickerUseCase - c6a02920-9695-11ea-aa61-000000000003", Dummy.successResponse) }
        coVerify(exactly = 1) { cacheManager.saveState("state - c6a02920-9695-11ea-aa61-000000000003", false) }
        coVerify(exactly = 0) { onSuccess.invoke(Dummy.successResponse.chatBundleSticker.list) }
        coVerify(exactly = 1) { onError.invoke(Dummy.error) }
    }
}
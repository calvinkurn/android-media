package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.TopchatCacheManagerStub
import com.tokopedia.topchat.TopchatTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
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
    private val cacheManager: TopchatCacheManagerStub = TopchatCacheManagerStub(Dummy.successResponse)
    private val testDispatcher: TopchatTestCoroutineContextDispatcher = TopchatTestCoroutineContextDispatcher()

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
        var needUpdate = false
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
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
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.cacheResponse.chatBundleSticker.list) }
    }
}
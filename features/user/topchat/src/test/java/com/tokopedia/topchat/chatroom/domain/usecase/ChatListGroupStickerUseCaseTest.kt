package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.TopchatCacheManagerStub
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatListGroupStickerUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var gqlUseCase: GraphqlUseCase<ChatListGroupStickerResponse>
    @RelaxedMockK
    lateinit var onLoading: (ChatListGroupStickerResponse) -> Unit
    @RelaxedMockK
    lateinit var onSuccess: (ChatListGroupStickerResponse, List<StickerGroup>) -> Unit
    @RelaxedMockK
    lateinit var onError: (Throwable) -> Unit
    private val cacheManager: TopchatCacheManagerStub = spyk(TopchatCacheManagerStub(Dummy.cacheResponse))
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private lateinit var useCase: ChatListGroupStickerUseCase

    object Dummy {
        val successResponse: ChatListGroupStickerResponse = FileUtil.parse(
                "/success_chat_list_group_sticker.json",
                ChatListGroupStickerResponse::class.java
        )
        val cacheResponse: ChatListGroupStickerResponse = FileUtil.parse(
                "/cache_chat_list_group_sticker.json",
                ChatListGroupStickerResponse::class.java
        )
        val successResponseSize2: ChatListGroupStickerResponse = FileUtil.parse(
                "/success_chat_list_group_sticker_size_2.json",
                ChatListGroupStickerResponse::class.java
        )
        var isSeller: Boolean = true
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ChatListGroupStickerUseCase(gqlUseCase, cacheManager, dispatchers)
        cacheManager.throwError = false
    }

    @Test
    fun `onLoading called with the previous cache`() {
        // Given
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { onLoading.invoke(Dummy.cacheResponse) }
    }

    @Test
    fun `onLoading is not called when cache is null`() {
        // Given
        cacheManager.cache = null
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 0) { onLoading.invoke(Dummy.cacheResponse) }
    }

    @Test
    fun `save cache when network response has different size for seller`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponseSize2
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { cacheManager.saveCache("ChatListGroupStickerUseCase - true", Dummy.successResponseSize2) }
    }

    @Test
    fun `save cache when network response has different size for buyer`() = runBlocking {
        // Given
        Dummy.isSeller = false
        cacheManager.cache = Dummy.cacheResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponseSize2
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { cacheManager.saveCache("ChatListGroupStickerUseCase - false", Dummy.successResponseSize2) }
    }

    @Test
    fun `save cache when network response has different lastUpdate for seller`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { cacheManager.saveCache("ChatListGroupStickerUseCase - true", Dummy.successResponse) }
    }

    @Test
    fun `network response is the same with the cache`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.successResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 0) { cacheManager.saveCache("ChatListGroupStickerUseCase - true", Dummy.successResponse) }
    }

    @Test
    fun `Need update onSuccess is not empty`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.cacheResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        assertThat(Dummy.successResponse.stickerGroups.size == 1)
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.successResponse, Dummy.successResponse.stickerGroups) }
    }

    @Test
    fun `Need update onSuccess is empty`() = runBlocking {
        // Given
        cacheManager.cache = Dummy.successResponse
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { onSuccess.invoke(Dummy.successResponse, emptyList()) }
    }
}
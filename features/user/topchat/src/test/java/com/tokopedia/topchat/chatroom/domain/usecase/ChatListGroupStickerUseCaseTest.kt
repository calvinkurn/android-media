package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.TopchatCacheManagerStub
import com.tokopedia.topchat.TopchatTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class ChatListGroupStickerUseCaseTest {

    @RelaxedMockK
    private lateinit var gqlUseCase: GraphqlUseCase<ChatListGroupStickerResponse>
    @RelaxedMockK
    lateinit var onLoading: (ChatListGroupStickerResponse) -> Unit
    @RelaxedMockK
    lateinit var onSuccess: (ChatListGroupStickerResponse, List<StickerGroup>) -> Unit
    @RelaxedMockK
    lateinit var onError: (Throwable) -> Unit
    private val cacheManager: TopchatCacheManagerStub = TopchatCacheManagerStub(Dummy.response)
    private val dispatchers: TopchatCoroutineContextProvider = TopchatTestCoroutineContextDispatcher()

    private lateinit var useCase: ChatListGroupStickerUseCase

    object Dummy {
        val response = ChatListGroupStickerResponse()
        val isSeller: Boolean = true
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
        cacheManager.throwError = false
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 1) { onLoading.invoke(Dummy.response) }
    }

    @Test
    fun `onLoading is not called when cache is null`() {
        // Given
        cacheManager.throwError = true
        // When
        useCase.getStickerGroup(Dummy.isSeller, onLoading, onSuccess, onError)
        // Then
        coVerify(exactly = 0) { onLoading.invoke(Dummy.response) }
    }
}
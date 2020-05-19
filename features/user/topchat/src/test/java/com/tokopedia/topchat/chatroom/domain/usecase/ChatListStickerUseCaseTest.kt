package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.TopchatCacheManagerStub
import com.tokopedia.topchat.TopchatTestCoroutineContextDispatcher
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.StickerResponse
import com.tokopedia.topchat.common.network.TopchatCacheManager
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatListStickerUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val gqlUseCase: GraphqlUseCase<StickerResponse> = mockk(relaxed = true)
    private val cacheManager: TopchatCacheManager = TopchatCacheManagerStub(StickerResponse())
    private val testDispatcher: TopchatTestCoroutineContextDispatcher = TopchatTestCoroutineContextDispatcher()

    private lateinit var chatListStickerUseCase: ChatListStickerUseCase

    object Dummy {
        const val stickerUUID = "c6a02920-9695-11ea-aa61-000000000003"
        val error = Throwable()
    }

    @Before
    fun setup() {
        chatListStickerUseCase = ChatListStickerUseCase(gqlUseCase, cacheManager, testDispatcher)
    }

    @Test
    fun `on success load sticker`() = runBlocking {
        val needUpdate = false
        chatListStickerUseCase.loadSticker(Dummy.stickerUUID, needUpdate, {}, {}, {})
        coVerify(exactly = 0) { gqlUseCase.executeOnBackground() }
        assert(true)
    }
}
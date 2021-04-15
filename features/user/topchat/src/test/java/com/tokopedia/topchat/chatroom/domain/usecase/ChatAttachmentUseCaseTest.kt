package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.FileUtil
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatAttachmentUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    @RelaxedMockK
    lateinit var gqlUseCase: GraphqlUseCase<ChatAttachmentResponse>
    @RelaxedMockK
    lateinit var onSuccess: (ArrayMap<String, Attachment>) -> Unit
    @RelaxedMockK
    lateinit var onError: (Throwable, ArrayMap<String, Attachment>) -> Unit
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider
    private val mapper: ChatAttachmentMapper = ChatAttachmentMapper()

    private lateinit var useCase: ChatAttachmentUseCase

    object Dummy {
        const val msgId: Long = 123123L
        const val attachmentId: String = "213213"

        val throwable = Throwable()
        val successResponse: ChatAttachmentResponse = FileUtil.parse(
                "/success_chat_list_group_sticker.json",
                ChatAttachmentResponse::class.java
        )
    }

    @Before
    fun before() {
        MockKAnnotations.init(this)
        useCase = ChatAttachmentUseCase(gqlUseCase, mapper, dispatchers)
    }

    @Test
    fun `on success get chat attachment`() {
        // Given
        coEvery { gqlUseCase.executeOnBackground() } returns Dummy.successResponse
        // When
        useCase.getAttachments(
                Dummy.msgId, Dummy.attachmentId, LocalCacheModel(),
                onSuccess, onError
        )
        // Then
        verify { onSuccess.invoke(any()) }
    }

    @Test
    fun `on error get chat attachment`() {
        // Given
        coEvery { gqlUseCase.executeOnBackground() } throws Dummy.throwable
        // When
        useCase.getAttachments(
                Dummy.msgId, Dummy.attachmentId, LocalCacheModel(), onSuccess, onError
        )
        // Then
        verify { onError.invoke(Dummy.throwable, any()) }
    }

}
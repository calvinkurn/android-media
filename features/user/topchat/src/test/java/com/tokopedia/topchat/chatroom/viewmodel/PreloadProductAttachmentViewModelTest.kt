package com.tokopedia.topchat.chatroom.viewmodel

import com.tokopedia.topchat.FileUtil
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class PreloadProductAttachmentViewModelTest: BaseTopChatViewModelTest() {

    val realChatMapper = ChatAttachmentMapper()
    lateinit var defaultPreAttachResponse: PreAttachPayloadResponse

    override fun before() {
        super.before()
        defaultPreAttachResponse = FileUtil.parse(
                "/success_pre_attach_payload_response.json",
                PreAttachPayloadResponse::class.java
        )
    }

    @Test
    fun should_pre_load_product_attachment() {
        // Given
        coEvery { chatPreAttachPayload(any()) } returns defaultPreAttachResponse
        coEvery { chatAttachmentMapper.map(any<PreAttachPayloadResponse>()) } returns realChatMapper.map(defaultPreAttachResponse)

        // When
        viewModel.loadProductPreview(listOf("1"))

        // Then
        assertEquals(viewModel.attachments.size, 1)
    }
}
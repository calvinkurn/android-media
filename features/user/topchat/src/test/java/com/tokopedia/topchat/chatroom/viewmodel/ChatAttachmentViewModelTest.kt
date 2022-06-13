package com.tokopedia.topchat.chatroom.viewmodel

import androidx.collection.ArrayMap
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.viewmodel.base.BaseTopChatViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class ChatAttachmentViewModelTest: BaseTopChatViewModelTest() {

    private val testAttachmentId = "123"
    private val testReplyId = "456"
    private val testProductId = "1160412120"

    override fun before() {
        super.before()
        viewModel.attachments.clear()
    }

    @Test
    fun success_get_attachment() {
        //Given
        val testProductAttachment = ProductAttachmentAttributes(
            productId = testProductId
        )
        val expectedResponse = ChatAttachmentResponse()
        val expectedMap = ArrayMap<String, Attachment>().also {
            val attachment = Attachment()
            attachment.parsedAttributes = testProductAttachment
            it[testAttachmentId] = attachment
        }
        coEvery {
            chatAttachmentUseCase(any())
        } returns expectedResponse
        every {
            chatAttachmentMapper.map(any<ChatAttachmentResponse>())
        } returns expectedMap

        //When
        viewModel.initUserLocation(null)
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        Assert.assertEquals(
            testProductId,
            (viewModel.chatAttachments.value?.get(testAttachmentId)?.parsedAttributes
                    as ProductAttachmentAttributes).productId
        )
    }

    @Test
    fun success_get_attachment_with_user_location() {
        //Given
        val testProductAttachment = ProductAttachmentAttributes(
            productId = testProductId
        )
        val testUserLocation = LocalCacheModel(
            lat = "testLat123",
            long = "testLong123",
            warehouse_id = "testWarehouse123"
        )
        val expectedResponse = ChatAttachmentResponse()
        val expectedMap = ArrayMap<String, Attachment>().also {
            val attachment = Attachment()
            attachment.parsedAttributes = testProductAttachment
            it[testAttachmentId] = attachment
        }
        coEvery {
            chatAttachmentUseCase(any())
        } returns expectedResponse
        every {
            chatAttachmentMapper.map(any<ChatAttachmentResponse>())
        } returns expectedMap

        //When
        viewModel.initUserLocation(testUserLocation)
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        Assert.assertEquals(
            testProductId,
            (viewModel.chatAttachments.value?.get(testAttachmentId)?.parsedAttributes
                    as ProductAttachmentAttributes).productId
        )
    }

    @Test
    fun success_get_attachment_with_user_location_without_long() {
        //Given
        val testProductAttachment = ProductAttachmentAttributes(
            productId = testProductId
        )
        val testUserLocation = LocalCacheModel(
            lat = "testLat123",
            long = "",
            warehouse_id = "testWarehouse123"
        )
        val expectedResponse = ChatAttachmentResponse()
        val expectedMap = ArrayMap<String, Attachment>().also {
            val attachment = Attachment()
            attachment.parsedAttributes = testProductAttachment
            it[testAttachmentId] = attachment
        }
        coEvery {
            chatAttachmentUseCase(any())
        } returns expectedResponse
        every {
            chatAttachmentMapper.map(any<ChatAttachmentResponse>())
        } returns expectedMap

        //When
        viewModel.initUserLocation(testUserLocation)
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        Assert.assertEquals(
            testProductId,
            (viewModel.chatAttachments.value?.get(testAttachmentId)?.parsedAttributes
                    as ProductAttachmentAttributes).productId
        )
    }

    @Test
    fun success_get_attachment_with_user_location_without_lat() {
        //Given
        val testProductAttachment = ProductAttachmentAttributes(
            productId = testProductId
        )
        val testUserLocation = LocalCacheModel(
            lat = "",
            long = "testLong123",
            warehouse_id = "testWarehouse123"
        )
        val expectedResponse = ChatAttachmentResponse()
        val expectedMap = ArrayMap<String, Attachment>().also {
            val attachment = Attachment()
            attachment.parsedAttributes = testProductAttachment
            it[testAttachmentId] = attachment
        }
        coEvery {
            chatAttachmentUseCase(any())
        } returns expectedResponse
        every {
            chatAttachmentMapper.map(any<ChatAttachmentResponse>())
        } returns expectedMap

        //When
        viewModel.initUserLocation(testUserLocation)
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        Assert.assertEquals(
            testProductId,
            (viewModel.chatAttachments.value?.get(testAttachmentId)?.parsedAttributes
                    as ProductAttachmentAttributes).productId
        )
    }

    @Test
    fun failed_get_attachment_empty_message_id() {
        //Given
        val emptyMessageId = 0L

        //When
        viewModel.loadAttachmentData(
            emptyMessageId,
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        coVerify(exactly = 0) {
            chatAttachmentUseCase(any())
        }
    }

    @Test
    fun failed_get_attachment_does_not_have_attachment() {
        //When
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel()
        )

        //Then
        coVerify(exactly = 0) {
            chatAttachmentUseCase(any())
        }
    }

    @Test
    fun failed_get_attachment_empty_msg_id_and_does_not_have_attachment() {
        //Given
        val emptyMessageId = 0L

        //When
        viewModel.loadAttachmentData(
            emptyMessageId,
            ChatroomViewModel()
        )

        //Then
        coVerify(exactly = 0) {
            chatAttachmentUseCase(any())
        }
    }

    @Test
    fun error_get_product_attachment() {
        //Given
        val expectedMap = ArrayMap<String, Attachment>().also {
            it[testAttachmentId] = ErrorAttachment()
        }
        coEvery {
            chatAttachmentUseCase(any())
        } throws expectedThrowable
        every {
            chatAttachmentMapper.mapError(any())
        } returns expectedMap

        //When
        viewModel.loadAttachmentData(
            testMessageId.toLongOrZero(),
            ChatroomViewModel(replyIDs = testReplyId)
        )

        //Then
        Assert.assertEquals(
            true,
            viewModel.chatAttachments.value?.get(testAttachmentId) is ErrorAttachment
        )
    }
}
package com.tokopedia.topchat.chatroom.view.presenter

import android.os.Build
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.pojo.ChatDelete
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exImageUploadId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exOpponentId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exSendMessage
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exStartTime
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.imageUploadViewModel
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.readParam
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.replyChatViewModelApiSuccess
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.source
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.toShopId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.toUserId
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketInfo
import io.mockk.*
import junit.framework.Assert
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.junit.Test
import rx.Observable
import rx.Subscriber
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class TopChatRoomPresenterTestCont : BaseTopChatRoomPresenterTest() {

    @Test
    fun `call onDestroy when start connect ws`() {
        // When
        presenter.connectWebSocket(exMessageId)

        // Then
        verify(exactly = 1) { presenter.destroyWebSocket() }
    }


    @Test
    fun `onOpen connect to webscoket`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(false) }
        verifyReadMessageSentToWs()
    }


    @Test
    fun `onReconnect webscoket`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsReconnect)

        // Then
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }


    @Test
    fun `onClose ws`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()

        // Then
        verify(exactly = 2) { presenter.destroyWebSocket() }
        verify(exactly = 1) { view.showErrorWebSocket(true) }
    }

    @Test
    fun `onMessage ws event reply when not in the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseReplyText)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReplyText)

        // Then
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 1) { view.hideUnreadMessage() }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verifyReadMessageSentToWs()
    }

    @Test
    fun `onMessage ws event reply when response has different msgId`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseReplyText)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)

        // When
        presenter.connectWebSocket("123")
        websocketServer.onNext(wsResponseReplyText)

        // Then
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(0))
        verify(exactly = 0) { view.hideUnreadMessage() }
        verify(exactly = 0) { view.onReceiveMessageEvent(wsChatVisitable) }
        verify(exactly = 0) { RxWebSocket.send(readParam, listInterceptor) }
    }

    @Test
    fun `should remove SRW bubble if receive image attachment event`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseImageAttachment)

        // Then
        verify(exactly = 1) { view.removeSrwBubble() }
    }

    @Test
    fun `should remove SRW bubble if receive product attachment event`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseProductAttachment)
        val wsChatVisitable = mockkWsMapper(wsChatPojo) as ProductAttachmentViewModel

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseProductAttachment)

        // Then
        verify(exactly = 1) { view.removeSrwBubble(wsChatVisitable.productId) }
    }

    @Test
    fun `onMessage ws event reply when in the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns true
        mockkParseResponse(wsResponseReplyText)

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReplyText)

        // Then
        MatcherAssert.assertThat(presenter.newUnreadMessage, equalTo(1))
        verify(exactly = 1) { view.showUnreadMessage(1) }
    }

    @Test
    fun `onMessage ws event typing when not the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseTyping)

        // Then
        verify(exactly = 1) { view.onReceiveStartTypingEvent() }
    }

    @Test
    fun `onMessage ws event end typing when not the middle of the page`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseEndTyping)

        // Then
        verify(exactly = 1) { view.onReceiveStopTypingEvent() }
    }

    @Test
    fun `onMessage ws event read message`() {
        // Given
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsResponseReadMessage)

        // Then
        verify(exactly = 1) { view.onReceiveReadEvent() }
    }

    @Test
    fun `check upload image using service`() {
        //Given
        val image = ImageUploadViewModel.Builder()
            .withMsgId(exMessageId)
            .withFromUid("123123")
            .withAttachmentId("123987")
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableViewModel.SENDING_TEXT)
            .withStartTime("123")
            .withIsDummy(true)
            .withImageUrl("https://ecs.tokopedia.com/image.jpg")
            .build()
        setFinalStatic(Build::class.java.getField("MODEL"), "samsung")
        every {
            UploadImageChatService.enqueueWork(
                any(),
                any(),
                any()
            )
        } returns Unit

        every {
            remoteConfig.getBoolean(any(), any())
        } returns true

        //When
        presenter.startUploadImages(image)

        //Then
        verify (exactly = 1) {
            view.addDummyMessage(image)
        }
    }

    @Test
    fun `check upload image problematic device`() {
        //Given
        val image = mockk<ImageUploadViewModel>(relaxed = true)
        every {
            remoteConfig.getBoolean(any(), any())
        } returns true

        setFinalStatic(Build::class.java.getField("MODEL"), "iris88")

        //When
        presenter.startUploadImages(image)

        //Then
        verify(exactly = 1) {
            uploadImageUseCase.upload(any(), any(), any())
            view.addDummyMessage(image)
        }
    }

    @Throws(Exception::class) //For mocking Build class
    fun setFinalStatic(field: Field, newValue: Any) {
        field.isAccessible = true

        val modifiersField = Field::class.java.getDeclaredField("modifiers")
        modifiersField.isAccessible = true
        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

        field.set(null, newValue)
    }

    @Test
    fun `check upload image failed to get remote config`() {
        //Given
        val image = mockk<ImageUploadViewModel>()
        val exception = mockk<Exception>("Oops!")
        every {
            remoteConfig.getBoolean(any(), any())
        } throws exception

        //When
        presenter.startUploadImages(image)

        //Then
        verify(exactly = 1) {
            uploadImageUseCase.upload(any(), any(), any())
            view.addDummyMessage(image)
        }
    }

    @Test
    fun `Get chat usecase called when load page`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()
        val roomMetaDataSlot = slot<(RoomMetaData) -> Unit>()
        every {
            getChatUseCase.getFirstPageChat(any(), any(), any(), capture(roomMetaDataSlot))
        } just Runs

        // When
        presenter.getExistingChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getFirstPageChat(
                exMessageId,
                mockOnSuccess,
                mockOnError,
                roomMetaDataSlot.captured
            )
        }
    }

    @Test
    fun `Get chat usecase called when load top page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadTopChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getTopChat(exMessageId, mockOnSuccess, mockOnError)
        }
    }

    @Test
    fun `Get chat usecase called when load bottom page chat`() {
        // Given
        val mockOnSuccess: (ChatroomViewModel, ChatReplies) -> Unit = mockk()
        val mockOnError: (Throwable) -> Unit = mockk()

        // When
        presenter.loadBottomChat(exMessageId, mockOnError, mockOnSuccess)

        // Then
        verify(exactly = 1) {
            getChatUseCase.getBottomChat(
                exMessageId,
                mockOnSuccess,
                mockOnError
            )
        }
    }

    @Test
    fun `On success get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                any(),
                capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onNext(GetTemplateUiModel())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onSuccessGetTemplate(emptyList()) }
    }

    @Test
    fun `On error get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                any(),
                capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onError(Throwable())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onErrorGetTemplate() }
    }

    @Test
    fun `on success upload image and sent through websocket`() {
        // Given
        every {
            remoteConfig.getBoolean(TopChatRoomPresenter.ENABLE_UPLOAD_IMAGE_SERVICE)
        } throws IllegalStateException("Failed to get remote config value")
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        val wsChatPojo = mockkParseResponse(wsResponseImageAttachment, false)
        val wsChatVisitable = mockkWsMapper(wsChatPojo)
        val websocketParam = TopChatWebSocketParam.generateParamSendImage(
            exMessageId, exImageUploadId, imageUploadViewModel
        )

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startCompressImages(imageUploadViewModel)
        websocketServer.onNext(wsResponseImageAttachment)

        // Then
        verify(exactly = 1) { view.addDummyMessage(imageUploadViewModel) }
        verify(exactly = 1) { RxWebSocket.send(websocketParam, listInterceptor) }
        verify(exactly = 1) { view.onReceiveMessageEvent(wsChatVisitable) }
        verify(exactly = 1) { view.removeDummy(imageUploadViewModel) }
    }

    @Test
    fun `on success upload image and sent through API`() {
        // Given
        val slot = slot<Subscriber<ReplyChatViewModel>>()
        every {
            remoteConfig.getBoolean(any())
        } returns false
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(),
                any()
            )
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(
                exImageUploadId,
                imageUploadViewModel
            )
        }
        every {
            replyChatUseCase.execute(any(), capture(slot))
        } answers {
            val subs = slot.captured
            subs.onNext(replyChatViewModelApiSuccess)
        }
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.onReceiveMessageEvent(replyChatViewModelApiSuccess.chat) }
        verify(exactly = 1) { view.removeDummy(imageUploadViewModel) }
    }

    @Test
    fun `on success upload image with service`() {
        val chatReply = mockk<ChatReplyPojo>()
        setFinalStatic(Build::class.java.getField("MODEL"), "samsung")
        every {
            remoteConfig.getBoolean(any())
        } returns true
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)

        every {
            uploadImageUseCase.upload(
                imageUploadViewModel, captureLambda(), any())
        } answers {
            val onSuccess = lambda<(String, ImageUploadViewModel) -> Unit>()
            onSuccess.invoke(exImageUploadId, imageUploadViewModel)
        }
        coEvery {
            replyChatGQLUseCase.replyMessage(
                any(),
                any(),
                any(),
                any()
            )
        } returns chatReply

        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        websocketServer.onNext(wsOpen)
        websocketServer.onCompleted()
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) {
            view.addDummyMessage(imageUploadViewModel)
        }
    }

    @Test
    fun `on error upload image`() {
        // Given
        val errorUploadImage = Throwable()
        every {
            remoteConfig.getBoolean(any())
        } returns false
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(true, ImageUtil.IMAGE_VALID)
        every {
            compressImageUseCase.compressImage(imageUploadViewModel.imageUrl!!)
        } returns Observable.just(imageUploadViewModel.imageUrl)
        every {
            uploadImageUseCase.upload(
                imageUploadViewModel,
                any(), captureLambda())
        } answers {
            val onError = lambda<(Throwable, ImageUploadViewModel) -> Unit>()
            onError.invoke(errorUploadImage, imageUploadViewModel)
        }

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.addDummyMessage(imageUploadViewModel) }
        verify(exactly = 1) {
            view.onErrorUploadImage(
                ErrorHandler.getErrorMessage(view.context, errorUploadImage),
                imageUploadViewModel
            )
        }
    }

    @Test
    fun `should have dummy image when upload image by service`() {
        // Given
        val imageViewModel = ImageUploadViewModel.Builder()
            .withMsgId(exMessageId)
            .withFromUid("123123")
            .withAttachmentId("123987")
            .withAttachmentType(AttachmentType.Companion.TYPE_IMAGE_UPLOAD)
            .withReplyTime(SendableViewModel.SENDING_TEXT)
            .withStartTime("123")
            .withIsDummy(true)
            .withImageUrl("https://ecs.tokopedia.com/image.jpg")
            .build()
        setFinalStatic(Build::class.java.getField("MODEL"), "samsung")
        every {
            UploadImageChatService.enqueueWork(any(), any(), any())
        } returns Unit
        every {
            remoteConfig.getBoolean(TopChatRoomPresenter.ENABLE_UPLOAD_IMAGE_SERVICE, any())
        } returns true

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startUploadImages(imageViewModel)

        // Then
        verify (exactly = 1) {
            view.addDummyMessage(imageViewModel)
        }
    }

    @Test
    fun `on error image file to upload validation IMAGE_UNDERSIZE`() {
        // Given
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(false, ImageUtil.IMAGE_UNDERSIZE)

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.showSnackbarError(view.getStringResource(R.string.undersize_image)) }
    }

    @Test
    fun `on error image file to upload validation IMAGE_EXCEED_SIZE_LIMIT`() {
        // Given
        every {
            ImageUtil.validateImageAttachment(imageUploadViewModel.imageUrl)
        } returns Pair(false, ImageUtil.IMAGE_EXCEED_SIZE_LIMIT)

        // When
        presenter.startCompressImages(imageUploadViewModel)

        // Then
        verify(exactly = 1) { view.showSnackbarError(view.getStringResource(R.string.oversize_image)) }
    }

    @Test
    fun `on uploading image`() {
        // Given
        every {
            uploadImageUseCase.isUploading
        } returns true

        // When
        presenter.isUploading()

        // Then
        verify(exactly = 1) { uploadImageUseCase.isUploading }
        Assert.assertTrue(uploadImageUseCase.isUploading)
    }

    @Test
    fun `on success send attachment and message through Websocket`() {
        // Given
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val paramSendMessage = "paramSendMessage"
        val paramSendAttachment = "paramSendAttachment"
        val paramStopTyping = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            TopChatWebSocketParam.generateParamSendMessage(
                any(), any(), any(), any(),
                any(), any(), any()
            )
        } returns paramSendMessage
        every {
            sendAbleProductPreview.generateMsgObj(
                any(), any(), any(), any()
            )
        } returns paramSendAttachment

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.connectWebSocket(exMessageId)
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exSendMessage, null
        )

        // Then
        verify(exactly = 1) { view.sendAnalyticAttachmentSent(sendAbleProductPreview) }
        verify(exactly = 1) { RxWebSocket.send(paramSendMessage, listInterceptor) }
        verify(exactly = 1) { RxWebSocket.send(paramSendAttachment, listInterceptor) }
        verify(exactly = 1) { RxWebSocket.send(paramStopTyping, listInterceptor) }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success delete chat`() {
        // Given
        val successDelete = ChatDelete(
            isSuccess = 1, detailResponse = "", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(successDelete)
        }

        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } returns result

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onSuccessDeleteConversation.invoke()
        }
    }

    @Test
    fun `on failed to delete chat`() {
        // Given
        val failedDelete = ChatDelete(
            isSuccess = 0, detailResponse = "Error", messageId = exMessageId.toLong())
        val result = ChatDeleteStatus().apply {
            this.chatMoveToTrash.list = listOf(failedDelete)
        }

        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } returns result

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onError.invoke(any())
        }
    }

    @Test
    fun `on error delete chat`() {
        // Given
        val throwable = mockk<Throwable>(relaxed = true)
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val onSuccessDeleteConversation: () -> Unit = mockk(relaxed = true)
        coEvery {
            moveChatToTrashUseCase.execute(exMessageId)
        } throws throwable

        // When
        presenter.deleteChat(exMessageId, onError, onSuccessDeleteConversation)

        // Then
        verify {
            onError.invoke(throwable)
        }
    }

    @Test
    fun `resend SRW from bubble attachment header`() {
        // Given
        val attachment = HeaderCtaButtonAttachment()
        val stopTypingParam = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendSrwFrom(attachment)

        // Then
        verify { RxWebSocket.send(stopTypingParam, listInterceptor) }
    }

    private fun mockkParseResponse(
        wsInfo: WebSocketInfo, isOpposite: Boolean = true
    ): ChatSocketPojo {
        val wsChatPojo = topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response).apply {
            this.isOpposite = isOpposite
        }
        every {
            topChatRoomWebSocketMessageMapper.parseResponse(wsInfo.response)
        } returns wsChatPojo
        return wsChatPojo
    }

    private fun mockkWsMapper(wsChatPojo: ChatSocketPojo): Visitable<*> {
        val wsChatVisitable = topChatRoomWebSocketMessageMapper.map(wsChatPojo)
        every { topChatRoomWebSocketMessageMapper.map(wsChatPojo) } returns wsChatVisitable
        return wsChatVisitable
    }

    private fun verifyReadMessageSentToWs() {
        verify(exactly = 1) { RxWebSocket.send(readParam, listInterceptor) }
    }
}
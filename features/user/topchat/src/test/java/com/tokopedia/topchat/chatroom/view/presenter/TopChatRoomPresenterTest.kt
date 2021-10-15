package com.tokopedia.topchat.chatroom.view.presenter

import androidx.collection.ArrayMap
import androidx.lifecycle.Observer
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ChatSettingsResponse
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouse
import com.tokopedia.topchat.chatroom.domain.pojo.tokonow.ChatTokoNowWarehouseResponse
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exMessageId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exOpponentId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exProductId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exResultProduct
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exSendMessage
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exShopId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exStartTime
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exSticker
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exUrl
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.exUserId
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.generateSendAbleInvoicePreview
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.generateSendAbleProductPreview
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.successGetChatListGroupSticker
import com.tokopedia.topchat.chatroom.view.presenter.BaseTopChatRoomPresenterTest.Dummy.successGetOrderProgressResponse
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.usecase.RequestParams
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import rx.Observable
import rx.Subscriber

class TopChatRoomPresenterTest : BaseTopChatRoomPresenterTest() {

    @Test
    fun `on success send sticker through websocket`() {
        // Given
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val stickerReq = slot<String>()
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every { RxWebSocket.send(capture(stickerReq), listInterceptor) } just Runs

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendAttachmentsAndSticker(
            exMessageId, exSticker, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify { RxWebSocket.send(stickerReq.captured, listInterceptor) }
        verify(exactly = 1) { mockOnSendingMessage.invoke() }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success send SRW preview through websocket`() {
        // Given
        val srwQuestion = QuestionUiModel()
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendAttachmentsAndSrw(
            exMessageId, srwQuestion, exStartTime, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { mockOnSendingMessage.invoke() }
        verify(exactly = 1) { view.clearAttachmentPreviews() }
    }

    @Test
    fun `on success send SRW bubble through websocket`() {
        // Given
        val srwQuestion = QuestionUiModel()
        val products = listOf(generateSendAbleProductPreview())
        val mockOnSendingMessage: () -> Unit = mockk(relaxed = true)
        val paramSendMessage = "paramSendMessage"
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false
        every {
            TopChatWebSocketParam.generateParamSendMessage(
                any(), any(), any(), any(), any(), any(), any()
            )
        } returns paramSendMessage

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.sendSrwBubble(
            exMessageId, srwQuestion, products, exOpponentId, mockOnSendingMessage
        )

        // Then
        verify(exactly = 1) { RxWebSocket.send(paramSendMessage, listInterceptor) }
    }

    @Test
    fun `on detachView`() {
        // When
        presenter.detachView()

        // Then
        verify {
            presenter.destroyWebSocket()
            getChatUseCase.unsubscribe()
            getTemplateChatRoomUseCase.unsubscribe()
            replyChatUseCase.unsubscribe()
            addToCartUseCase.unsubscribe()
            groupStickerUseCase.safeCancel()
            chatAttachmentUseCase.safeCancel()
        }
    }

    @Test
    fun `send ws event on start typing`() {
        //Given
        val typingParam = TopChatWebSocketParam.generateParamStartTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.startTyping()

        // Then
        verify { RxWebSocket.send(typingParam, listInterceptor) }
    }

    @Test
    fun `send ws event on stop typing`() {
        //Given
        val stopTypingParam = TopChatWebSocketParam.generateParamStopTyping(exMessageId)
        every { webSocketUtil.getWebSocketInfo(any(), any()) } returns websocketServer
        every { getChatUseCase.isInTheMiddleOfThePage() } returns false

        // When
        presenter.connectWebSocket(exMessageId)
        presenter.stopTyping()

        // Then
        verify { RxWebSocket.send(stopTypingParam, listInterceptor) }
    }

    @Test
    fun `check hasEmptyAttachmentPreview`() {
        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        val isEmptyAttachment = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(!isEmptyAttachment)
    }

    @Test
    fun `should send JsonObject attachment preview`() {
        // Given
        val msgObj = JsonObject()
        every {
            sendAbleProductPreview.generateMsgObj(any(), any(), any(), any(), any(), any())
        } returns msgObj

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exMessageId, exSendMessage, exStartTime, exOpponentId
        ) {}

        // Then
        verify(exactly = 1) { RxWebSocket.send(msgObj, listInterceptor) }
    }

    @Test
    fun `should send request string attachment preview`() {
        // Given
        val msgAttachment = CommonUtil.toJson("WebsocketVoucherPayload")
        every {
            sendAbleProductPreview.generateMsgObj(any(), any(), any(), any(), any(), any())
        } returns msgAttachment

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.sendAttachmentsAndMessage(
            exMessageId, exSendMessage, exStartTime, exOpponentId
        ) {}

        // Then
        verify(exactly = 1) { RxWebSocket.send(msgAttachment, listInterceptor) }
    }

    @Test
    fun `on initAttachmentPreview`() {
        // Given
        val attachmentList = arrayListOf(sendAbleProductPreview)

        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.initAttachmentPreview()

        // Then
        verify(exactly = 1) {
            view.showAttachmentPreview(attachmentList)
            view.focusOnReply()
        }
    }

    @Test
    fun `on clearAttachmentPreview`() {
        // When
        presenter.addAttachmentPreview(sendAbleProductPreview)
        presenter.clearAttachmentPreview()
        val isEmptyAttachmentPreview = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(isEmptyAttachmentPreview)
    }

    @Test
    fun `on initProductPreviewFromAttachProduct`() {
        // Given
        val productPreview = arrayListOf(exResultProduct)

        // When
        presenter.initProductPreviewFromAttachProduct(productPreview)
        val isEmptyAttachmentPreview = presenter.hasEmptyAttachmentPreview()

        // Then
        assert(!isEmptyAttachmentPreview)
        verify(exactly = 1) {
            presenter.initAttachmentPreview()
        }
    }

    @Test
    fun `on success click banned product seamless`() {
        // Given
        val liteUrl = "https://tokopedia/lite/url"
        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(liteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onUrlGenerated(liteUrl)
        }

        // When
        presenter.onClickBannedProduct(liteUrl)

        // Then
        verify(exactly = 1) {
            view.redirectToBrowser(liteUrl)
        }
    }

    @Test
    fun `on error click banned product seamless`() {
        // Given
        val liteUrl = "https://tokopedia/lite/url"
        val slot = slot<SeamlessLoginSubscriber>()
        every {
            seamlessLoginUsecase.generateSeamlessUrl(liteUrl, capture(slot))
        } answers {
            val subs = slot.captured
            subs.onError(liteUrl)
        }

        // When
        presenter.onClickBannedProduct(liteUrl)

        // Then
        verify(exactly = 1) {
            view.redirectToBrowser(liteUrl)
        }
    }

    @Test
    fun `on loadChatRoomSettings`() {
        // Given
        val onSuccess: (List<Visitable<TopChatTypeFactory>>) -> Unit = mockk(relaxed = true)

        // When
        presenter.loadChatRoomSettings(exMessageId, onSuccess)

        // Then
        verify(exactly = 1) {
            getChatRoomSettingUseCase.execute(exMessageId, onSuccess)
        }
    }

    @Test
    fun `on toggle add and remove WishList`() {
        // Given
        val wishlistActionListener: WishListActionListener = mockk(relaxed = true)

        // When
        presenter.addToWishList(exProductId, exUserId, wishlistActionListener)
        presenter.removeFromWishList(exProductId, exUserId, wishlistActionListener)

        // Then
        verify(exactly = 1) {
            addWishListUseCase.createObservable(exProductId, exUserId, wishlistActionListener)
            removeWishListUseCase.createObservable(exProductId, exUserId, wishlistActionListener)
        }
    }

    @Test
    fun `on success get order progress`() {
        // Given
        every { orderProgressUseCase.getOrderProgress(any(), captureLambda(), any()) } answers {
            val onSuccess = lambda<(OrderProgressResponse) -> Unit>()
            onSuccess.invoke(successGetOrderProgressResponse)
        }

        // When
        presenter.getOrderProgress(exMessageId)

        // Then
        verify(exactly = 1) {
            view.renderOrderProgress(successGetOrderProgressResponse.chatOrderProgress)
        }
    }

    @Test
    fun `on success get sticker group list`() {
        // Given
        val roomModel = ChatroomViewModel()
        val needTopUpdateCache = emptyList<StickerGroup>()
        val onLoadingSlot = slot<(ChatListGroupStickerResponse) -> Unit>()
        val onSuccessSlot = slot<(ChatListGroupStickerResponse, List<StickerGroup>) -> Unit>()
        every {
            groupStickerUseCase.getStickerGroup(
                roomModel.isSeller(),
                capture(onLoadingSlot),
                capture(onSuccessSlot),
                any()
            )
        } answers {
            val onLoading = onLoadingSlot.captured
            onLoading.invoke(successGetChatListGroupSticker)
            val onSuccess = onSuccessSlot.captured
            onSuccess.invoke(successGetChatListGroupSticker, needTopUpdateCache)
        }

        // When
        presenter.getStickerGroupList(roomModel)

        // Then
        verify(exactly = 2) {
            view.getChatMenuView()
        }
    }

    @Test
    fun `on success loadAttachmentData`() {
        // Given
        val roomModel = ChatroomViewModel(replyIDs = "3213, 3123")
        val mapSuccessAttachment = ArrayMap<String, Attachment>().apply {
            put("test_attachment", Attachment())
        }
        every {
            chatAttachmentUseCase.getAttachments(
                exMessageId.toLongOrZero(), roomModel.replyIDs,
                any(), captureLambda(), any()
            )
        } answers {
            val onSuccess = lambda<(ArrayMap<String, Attachment>) -> Unit>()
            onSuccess.invoke(mapSuccessAttachment)
        }

        // When
        presenter.initUserLocation(null)
        presenter.initUserLocation(LocalCacheModel())
        presenter.loadAttachmentData(exMessageId.toLongOrZero(), roomModel)

        // Then
        val attachments = presenter.attachments
        verify(exactly = 1) { view.updateAttachmentsView(attachments) }
        assertTrue(presenter.attachments.size == 1)
    }

    @Test
    fun `on error loadAttachmentData`() {
        // Given
        val roomModel = ChatroomViewModel(replyIDs = "3213, 3123")
        val mapErrorAttachment = ArrayMap<String, Attachment>().apply {
            put("test_error_attachment", Attachment())
        }
        val throwable = Throwable()
        every {
            chatAttachmentUseCase.getAttachments(
                exMessageId.toLongOrZero(), roomModel.replyIDs, any(),
                any(), captureLambda()
            )
        } answers {
            val onError = lambda<(Throwable, ArrayMap<String, Attachment>) -> Unit>()
            onError.invoke(throwable, mapErrorAttachment)
        }

        // When
        presenter.loadAttachmentData(exMessageId.toLongOrZero(), roomModel)

        // Then
        val attachments = presenter.attachments
        verify(exactly = 1) { view.updateAttachmentsView(attachments) }
        assertTrue(presenter.attachments.size == 1)
    }

    @Test
    fun `check setBeforeReplyTime`() {
        //Given
        val exCreateTime = "1234532"

        // When
        presenter.setBeforeReplyTime(exCreateTime)

        // Then
        verify(exactly = 1) { getChatUseCase.minReplyTime = exCreateTime }
    }

    @Test
    fun `check resetChatUseCase`() {
        // When
        presenter.resetChatUseCase()

        // Then
        verify(exactly = 1) { getChatUseCase.reset() }
    }

    @Test
    fun `check resetUnreadMessage`() {
        // When
        presenter.resetUnreadMessage()

        // Then
        assertTrue(presenter.newUnreadMessage == 0)
    }

    @Test
    fun `check requestBlockPromo`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.requestBlockPromo(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.blockPromo(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check requestAllowPromo`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.requestAllowPromo(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.allowPromo(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check blockChat`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.blockChat(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.blockChat(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `check unBlockChat`() {
        // Given
        val onSuccess: (ChatSettingsResponse) -> Unit = mockk()
        val onError: (Throwable) -> Unit = mockk()

        // When
        presenter.unBlockChat(exMessageId, onSuccess, onError)

        // Then
        verify(exactly = 1) {
            chatToggleBlockChat.unBlockChat(exMessageId, onSuccess, onError)
        }
    }

    @Test
    fun `on load background from cache`() {
        // Given
        every {
            chatBackgroundUseCase.getBackground(
                captureLambda(), any(), any()
            )
        } answers {
            val onCache = lambda<(String) -> Unit>()
            onCache.invoke(exUrl)
        }

        // When
        presenter.getBackground()

        // Then
        verify(exactly = 1) {
            view.renderBackground(exUrl)
        }
    }

    @Test
    fun `on load background from success response and need to update`() {
        // Given
        every {
            chatBackgroundUseCase.getBackground(
                any(), captureLambda(), any()
            )
        } answers {
            val onSuccess = lambda<(String, Boolean) -> Unit>()
            onSuccess.invoke(exUrl, true)
        }

        // When
        presenter.getBackground()

        // Then
        verify(exactly = 1) {
            view.renderBackground(exUrl)
        }
    }

    @Test
    fun `when success addProductToCart`() {
        // Given
        val onSuccess: (data: DataModel) -> Unit = mockk(relaxed = true)
        val successAtc = getSuccessAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(successAtc)

        // When
        presenter.addProductToCart(RequestParams(), onSuccess, {})

        // Then
        verify(exactly = 1) {
            onSuccess.invoke(successAtc.data)
        }
    }

    @Test
    fun `when error addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorAtc = getErrorAtcModel()
        every {
            addToCartUseCase.createObservable(any())
        } returns Observable.just(errorAtc)

        // When
        presenter.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke("Gagal menambahkan produk")
        }
    }

    @Test
    fun `when error throwable addProductToCart`() {
        // Given
        val onError: (msg: String) -> Unit = mockk(relaxed = true)
        val errorMsg = "Gagal menambahkan produk"
        every {
            addToCartUseCase.createObservable(any())
        } throws IllegalStateException(errorMsg)

        // When
        presenter.addProductToCart(RequestParams(), {}, onError)

        // Then
        verify(exactly = 1) {
            onError.invoke(errorMsg)
        }
    }

    @Test
    fun `success load srw`() {
        // Given
        val observer: Observer<Resource<ChatSmartReplyQuestionResponse>> = mockk()
        val expectedValue: Resource<ChatSmartReplyQuestionResponse> = Resource.success(
            ChatSmartReplyQuestionResponse()
        )
        val successFlow = flow { emit(expectedValue) }
        every {
            chatSrwUseCase.getSrwList(exMessageId)
        } returns successFlow

        // When
        presenter.srw.observeForever(observer)
        presenter.getSmartReplyWidget(exMessageId)

        // Then
        verify(exactly = 1) {
            observer.onChanged(expectedValue)
        }
    }

    @Test
    fun `error load srw`() {
        // Given
        val observer: Observer<Resource<ChatSmartReplyQuestionResponse>> = mockk()
        val throwable = IllegalStateException()
        val expectedValue: Resource<ChatSmartReplyQuestionResponse> = Resource.error(
            throwable, null
        )
        every {
            chatSrwUseCase.getSrwList(exMessageId)
        } throws throwable

        // When
        presenter.srw.observeForever(observer)
        presenter.getSmartReplyWidget(exMessageId)

        // Then
        verify(exactly = 1) {
            observer.onChanged(expectedValue)
        }
    }

    @Test
    fun `onGoingStockUpdate added`() {
        // Given
        val productId = "123"
        val product = ProductAttachmentViewModel.Builder().build()

        // When
        presenter.addOngoingUpdateProductStock(productId, product, 0, null)

        // Then
        assertThat(presenter.onGoingStockUpdate.containsKey(productId), `is`(true))
        assertThat(presenter.onGoingStockUpdate.size, `is`(1))
    }

    @Test
    fun `should filter product id on attachment preview`() {
        // Given
        val product = generateSendAbleProductPreview()
        val invoice = generateSendAbleInvoicePreview()

        // When
        presenter.addAttachmentPreview(product)
        presenter.addAttachmentPreview(invoice)
        val productIds = presenter.getProductIdPreview()
        val attachmentPreviews = presenter.getAttachmentsPreview()

        // Then
        assertThat(productIds.size, `is`(1))
        assertThat(attachmentPreviews.size, `is`(2))
        assertThat(productIds, hasItem("12398764"))
    }

    @Test
    fun `get interlocutor warehouse id`() {
        // Given
        val warehouseId = "123"
        val response = ChatTokoNowWarehouseResponse(
            ChatTokoNowWarehouse(warehouseId = warehouseId)
        )
        val expectedValue = Resource.success(response)
        val successFlow = flow { emit(expectedValue) }
        every {
            tokoNowWHUsecase.getWarehouseId(exMessageId)
        } returns successFlow

        // When
        presenter.adjustInterlocutorWarehouseId(exMessageId)

        // Then
        assertThat(presenter.attachProductWarehouseId, `is`(warehouseId))
    }

    private fun getErrorAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 0
            data.message.add("Gagal menambahkan produk")
        }
    }

    private fun getSuccessAtcModel(): AddToCartDataModel {
        return AddToCartDataModel().apply {
            data.success = 1
        }
    }

}
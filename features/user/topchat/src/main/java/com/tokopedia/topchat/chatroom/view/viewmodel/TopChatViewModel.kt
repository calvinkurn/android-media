package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.*
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.GetChatResult
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ActionType
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.BlockActionType
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.WrapperChatSetting
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.domain.usecase.GetReminderTickerUseCase.Param.Companion.SRW_TICKER
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

class TopChatViewModel @Inject constructor(
    private var getExistingMessageIdUseCase: GetExistingMessageIdUseCase,
    private var getShopFollowingUseCase: GetShopFollowingUseCase,
    private var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
    private var addToCartUseCase: AddToCartUseCase,
    private var seamlessLoginUsecase: SeamlessLoginUsecase,
    private var getChatRoomSettingUseCase: GetChatRoomSettingUseCase,
    private var orderProgressUseCase: OrderProgressUseCase,
    private var reminderTickerUseCase: GetReminderTickerUseCase,
    private var closeReminderTicker: CloseReminderTicker,
    private var addToCartOccUseCase: AddToCartOccMultiUseCase,
    private val chatToggleBlockChat: ChatToggleBlockChatUseCase,
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    private val getChatBackgroundUseCase: GetChatBackgroundUseCase,
    private val chatAttachmentUseCase: ChatAttachmentUseCase,
    private val getChatListGroupStickerUseCase: GetChatListGroupStickerUseCase,
    private val chatSrwUseCase: GetSmartReplyQuestionUseCase,
    private val tokoNowWHUsecase: GetChatTokoNowWarehouseUseCase,
    private var addWishListUseCase: AddWishListUseCase,
    private var removeWishListUseCase: RemoveWishListUseCase,
    private var getChatUseCase: GetChatUseCase,
    private var unsendReplyUseCase: UnsendReplyUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig,
    private val chatAttachmentMapper: ChatAttachmentMapper,
    private val existingChatMapper: TopChatRoomGetExistingChatMapper,
    private val chatWebSocket: TopchatWebSocket,
    private val webSocketStateHandler: WebSocketStateHandler,
    private val webSocketParser: WebSocketParser,
    private var topChatRoomWebSocketMessageMapper: TopChatRoomWebSocketMessageMapper,
    private var payloadGenerator: WebsocketPayloadGenerator
) : BaseViewModel(dispatcher.main), LifecycleObserver {

    private val _messageId = MutableLiveData<Result<String>>()
    val messageId: LiveData<Result<String>>
        get() = _messageId

    private val _shopFollowing = MutableLiveData<Result<ShopFollowingPojo>>()
    val shopFollowing: LiveData<Result<ShopFollowingPojo>>
        get() = _shopFollowing

    private val _followUnfollowShop =
        MutableLiveData<Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>>()
    val followUnfollowShop: LiveData<Pair<BroadcastSpamHandlerUiModel?, Result<Boolean>>>
        get() = _followUnfollowShop

    private val _addToCart = MutableLiveData<Result<AddToCartParam>>()
    val addToCart: LiveData<Result<AddToCartParam>>
        get() = _addToCart

    private val _seamlessLogin = MutableLiveData<String>()
    val seamlessLogin: LiveData<String>
        get() = _seamlessLogin

    private val _chatRoomSetting = MutableLiveData<Result<RoomSettingResponse>>()
    val chatRoomSetting: LiveData<Result<RoomSettingResponse>>
        get() = _chatRoomSetting

    private val _orderProgress = MutableLiveData<Result<OrderProgressResponse>>()
    val orderProgress: LiveData<Result<OrderProgressResponse>>
        get() = _orderProgress

    private val _srwTickerReminder = MutableLiveData<Result<ReminderTickerUiModel>>()
    val srwTickerReminder: LiveData<Result<ReminderTickerUiModel>>
        get() = _srwTickerReminder

    private val _occProduct = MutableLiveData<Result<ProductAttachmentUiModel>>()
    val occProduct: LiveData<Result<ProductAttachmentUiModel>>
        get() = _occProduct

    private val _toggleBlock = MutableLiveData<WrapperChatSetting>()
    val toggleBlock: LiveData<WrapperChatSetting>
        get() = _toggleBlock

    private val _chatDeleteStatus = MutableLiveData<Result<ChatDeleteStatus>>()
    val chatDeleteStatus: LiveData<Result<ChatDeleteStatus>>
        get() = _chatDeleteStatus

    private val _chatBackground = MutableLiveData<Result<String>>()
    val chatBackground: MutableLiveData<Result<String>>
        get() = _chatBackground

    private val _chatAttachments = MutableLiveData<ArrayMap<String, Attachment>>()
    val chatAttachments: MutableLiveData<ArrayMap<String, Attachment>>
        get() = _chatAttachments

    private val _chatListGroupSticker =
        MutableLiveData<Result<Pair<ChatListGroupStickerResponse, List<StickerGroup>>>>()
    val chatListGroupSticker: MutableLiveData<Result<Pair<ChatListGroupStickerResponse, List<StickerGroup>>>>
        get() = _chatListGroupSticker

    private val _srw = MutableLiveData<Resource<ChatSmartReplyQuestionResponse>>()
    val srw: LiveData<Resource<ChatSmartReplyQuestionResponse>>
        get() = _srw

    private val _existingChat = MutableLiveData<Pair<Result<GetChatResult>, Boolean>>()
    val existingChat: LiveData<Pair<Result<GetChatResult>, Boolean>>
        get() = _existingChat

    private val _topChat = MutableLiveData<Result<GetChatResult>>()
    val topChat: LiveData<Result<GetChatResult>>
        get() = _topChat

    private val _bottomChat = MutableLiveData<Result<GetChatResult>>()
    val bottomChat: LiveData<Result<GetChatResult>>
        get() = _bottomChat

    private val _deleteBubble = MutableLiveData<Result<String>>()
    val deleteBubble: LiveData<Result<String>>
        get() = _deleteBubble

    private var autoRetryJob: Job? = null
    private val _isWebsocketError = MutableLiveData<Boolean>()
    val isWebsocketError: LiveData<Boolean>
        get() = _isWebsocketError

    private val _isTyping = MutableLiveData<Boolean>()
    val isTyping: LiveData<Boolean>
        get() = _isTyping

    private val _msgDeleted = MutableLiveData<String>()
    val msgDeleted: LiveData<String>
        get() = _msgDeleted

    private val _msgRead = MutableLiveData<Unit>()
    val msgRead: LiveData<Unit>
        get() = _msgRead

    private val _unreadMsg = MutableLiveData<Int>()
    val unreadMsg: LiveData<Int>
        get() = _unreadMsg

    private val _newMsg = MutableLiveData<Visitable<*>>()
    val newMsg: LiveData<Visitable<*>>
        get() = _newMsg

    private val _removeSrwBubble = MutableLiveData<String?>()
    val removeSrwBubble: LiveData<String?>
        get() = _removeSrwBubble

    private val _previewMsg = MutableLiveData<SendableUiModel>()
    val previewMsg: LiveData<SendableUiModel>
        get() = _previewMsg

    var attachProductWarehouseId = "0"
    val attachments: ArrayMap<String, Attachment> = ArrayMap()
    var roomMetaData: RoomMetaData = RoomMetaData()
    private var userLocationInfo = LocalCacheModel()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        chatWebSocket.close()
        chatWebSocket.destroy()
        cancel()
    }

    fun connectWebSocket() {
        chatWebSocket.connectWebSocket(object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("$TAG - onOpen")
                handleOnOpenWebSocket()
                // TODO: add mark as read
                // readMessage()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val response = webSocketParser.parseResponse(text)
                handleOnMessageWebSocket(response)
                Timber.d("$TAG - onMessage - ${response.code}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosing - $code - $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosed - $code - $reason")
                handleOnClosedWebSocket(code)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.d("$TAG - onFailure - ${t.message}")
                handleOnFailureWebSocket()
            }
        })
    }

    private fun handleOnOpenWebSocket() {
        _isWebsocketError.postValue(false)
        webSocketStateHandler.retrySucceed()
    }

    private fun handleOnMessageWebSocket(response: WebSocketResponse) {
        val incomingChatEvent = topChatRoomWebSocketMessageMapper.parseResponse(response)
        if (incomingChatEvent.msgId.toString() != roomMetaData.msgId) return
        when (response.code) {
            WebsocketEvent.Event.EVENT_TOPCHAT_TYPING -> onReceiveTypingEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING -> onReceiveEndTypingEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE -> onReceiveReadMsgEvent()
            WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE -> onReceiveReplyEvent(
                incomingChatEvent
            )
            WebsocketEvent.Event.EVENT_DELETE_MSG -> onReceiveDeleteMsgEvent(
                incomingChatEvent
            )
        }
    }

    private fun onReceiveDeleteMsgEvent(chat: ChatSocketPojo) {
        _msgDeleted.postValue(chat.replyTime)
    }

    private fun onReceiveReplyEvent(chat: ChatSocketPojo) {
        if (!isInTheMiddleOfThePage()) {
            renderChatItem(chat)
            _unreadMsg.postValue(0)
        } else {
            if (chat.isOpposite) {
                incrementUnreadMsg()
            }
        }
    }

    // TODO: check upload image
    private fun renderChatItem(chat: ChatSocketPojo) {
        val chatUiModel = topChatRoomWebSocketMessageMapper.map(chat)
        _newMsg.postValue(chatUiModel)
        handleSrwBubbleState(chat, chatUiModel)
//        if (!pojo.isOpposite) {
//            checkDummyAndRemove(uiModel)
//        } else {
//            readMessage()
//        }
    }

    private fun handleSrwBubbleState(pojo: ChatSocketPojo, uiModel: Visitable<*>) {
        when (pojo.attachment?.type) {
            AttachmentType.Companion.TYPE_INVOICE_SEND,
            AttachmentType.Companion.TYPE_IMAGE_UPLOAD,
            AttachmentType.Companion.TYPE_VOUCHER -> _removeSrwBubble.postValue(null)
            AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT -> {
                if (uiModel is ProductAttachmentUiModel) {
                    _removeSrwBubble.postValue(uiModel.productId)
                }
            }
        }
    }

    private fun incrementUnreadMsg() {
        val currentValue = _unreadMsg.value ?: 0
        _unreadMsg.postValue(currentValue + 1)
    }

    private fun onReceiveReadMsgEvent() {
        if (!isInTheMiddleOfThePage()) {
            _msgRead.postValue(Unit)
        }
    }

    private fun onReceiveEndTypingEvent() {
        _isTyping.postValue(false)
    }

    private fun onReceiveTypingEvent() {
        _isTyping.postValue(true)
    }

    private fun handleOnClosedWebSocket(code: Int) {
        if (code != DefaultTopChatWebSocket.CODE_NORMAL_CLOSURE) {
            retryConnectWebSocket()
        }
    }

    fun resetUnreadMessage() {
        _unreadMsg.postValue(0)
    }

    private fun handleOnFailureWebSocket() {
        retryConnectWebSocket()
    }

    private fun retryConnectWebSocket() {
        chatWebSocket.close()
        _isWebsocketError.postValue(true)
        autoRetryJob = launchCatchError(
            dispatcher.io,
            {
                Timber.d("$TAG - scheduleForRetry")
                webSocketStateHandler.scheduleForRetry {
                    withContext(dispatcher.main) {
                        Timber.d("$TAG - reconnecting websocket")
                        connectWebSocket()
                    }
                }
            },
            {
                Timber.d("$TAG - ${it.message}")
            }
        )
    }

    fun initUserLocation(userLocation: LocalCacheModel?) {
        userLocation ?: return
        this.userLocationInfo = userLocation
        this.attachProductWarehouseId = userLocation.warehouse_id
    }

    fun getMessageId(
        toUserId: String,
        toShopId: String,
        source: String,
    ) {
        launchCatchError(block = {
            val existingMessageIdParam = GetExistingMessageIdUseCase.Param(
                toUserId = toUserId,
                toShopId = toShopId,
                source = source
            )
            val result = getExistingMessageIdUseCase(existingMessageIdParam)
            _messageId.value = Success(result.chatExistingChat.messageId)
            roomMetaData.updateMessageId(result.chatExistingChat.messageId)
        }, onError = {
            _messageId.value = Fail(it)
        })
    }

    fun getShopFollowingStatus(shopId: Long) {
        launchCatchError(block = {
            val result = getShopFollowingUseCase(shopId)
            _shopFollowing.value = Success(result)
        }, onError = {
            _shopFollowing.value = Fail(it)
        })
    }

    fun followUnfollowShop(
        shopId: String,
        action: ToggleFavouriteShopUseCase.Action? = null,
        element: BroadcastSpamHandlerUiModel? = null
    ) {
        launchCatchError(block = {
            val param = if (action != null) {
                ToggleFavouriteShopUseCase.createRequestParam(shopId, action)
            } else {
                ToggleFavouriteShopUseCase.createRequestParam(shopId)
            }
            withContext(dispatcher.io) {
                val result = toggleFavouriteShopUseCase
                    .createObservable(requestParams = param)
                    .toBlocking()
                    .first()
                _followUnfollowShop.postValue(Pair(element, Success(result)))
            }
        }, onError = {
            _followUnfollowShop.value = Pair(element, Fail(it))
        })
    }

    fun addProductToCart(addToCartParam: AddToCartParam) {
        launchCatchError(block = {
            setupAddToCartParam(addToCartParam)
            val atcResult = addToCartUseCase.executeOnBackground()
            if (atcResult.data.success == 1) {
                addToCartParam.dataModel = atcResult.data
                _addToCart.value = Success(addToCartParam)
            } else {
                _addToCart.value = Fail(MessageErrorException(atcResult.errorMessage.first()))
            }
        }, onError = {
            _addToCart.value = Fail(it)
        })
    }

    private fun setupAddToCartParam(addToCartParam: AddToCartParam) {
        val addToCartRequestParams = AddToCartRequestParams(
            productId = addToCartParam.productId.toLongOrZero(),
            shopId = addToCartParam.shopId.toInt(),
            quantity = addToCartParam.minOrder,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_TOPCHAT
        )
        addToCartUseCase.addToCartRequestParams = addToCartRequestParams
    }

    fun onClickBannedProduct(liteUrl: String) {
        val seamlessLoginSubscriber = createSeamlessLoginSubscriber(liteUrl)
        seamlessLoginUsecase.generateSeamlessUrl(liteUrl, seamlessLoginSubscriber)
    }

    private fun createSeamlessLoginSubscriber(liteUrl: String): SeamlessLoginSubscriber {
        return object : SeamlessLoginSubscriber {
            override fun onUrlGenerated(url: String) {
                _seamlessLogin.value = url
            }

            override fun onError(msg: String) {
                _seamlessLogin.value = liteUrl
            }
        }
    }

    fun loadChatRoomSettings(messageId: String) {
        launchCatchError(block = {
            val result = getChatRoomSettingUseCase(messageId)
            _chatRoomSetting.value = Success(result)
        }, onError = {
            _chatRoomSetting.value = Fail(it)
        })
    }

    fun getOrderProgress(messageId: String) {
        launchCatchError(block = {
            val result = orderProgressUseCase(messageId)
            _orderProgress.value = Success(result)
        }, onError = {
            _orderProgress.value = Fail(it)
        })
    }

    fun getTickerReminder() {
        launchCatchError(
            block = {
                val existingMessageIdParam = GetReminderTickerUseCase.Param(
                    featureId = SRW_TICKER
                )
                val result = reminderTickerUseCase(existingMessageIdParam)
                _srwTickerReminder.value = Success(result.getReminderTicker)
            },
            onError = { }
        )
    }

    fun removeTicker() {
        _srwTickerReminder.value = null
    }

    fun closeTickerReminder(element: ReminderTickerUiModel) {
        launchCatchError(
            block = {
                val existingMessageIdParam = GetReminderTickerUseCase.Param(
                    featureId = element.featureId
                )
                closeReminderTicker(existingMessageIdParam)
            },
            onError = { }
        )

    }

    fun occProduct(
        userId: String,
        product: ProductAttachmentUiModel
    ) {
        launchCatchError(block = {
            val params = getAddToCartOccMultiRequestParams(userId, product)
            addToCartOccUseCase.setParams(params)
            val result = addToCartOccUseCase.executeOnBackground()
            if (result.isStatusError()) {
                _occProduct.value = Fail(MessageErrorException(result.getAtcErrorMessage()))
            } else {
                if (result.data.cart.isNotEmpty()) {
                    product.cartId = result.data.cart.first().cartId
                }
                _occProduct.value = Success(product)
            }
        }, onError = {
            _occProduct.value = Fail(it)
        })
    }

    private fun getAddToCartOccMultiRequestParams(
        userId: String,
        product: ProductAttachmentUiModel
    ): AddToCartOccMultiRequestParams {
        return AddToCartOccMultiRequestParams(
            carts = listOf(
                AddToCartOccMultiCartParam(
                    productId = product.productId,
                    shopId = product.shopId.toString(),
                    quantity = product.minOrder.toString(),
                    //analytics data
                    productName = product.productName,
                    category = product.category,
                    price = product.productPrice
                )
            ),
            userId = userId
        )
    }

    fun toggleBlockChatPromo(
        messageId: String,
        blockActionType: ActionType,
        element: BroadcastSpamHandlerUiModel? = null
    ) {
        launchCatchError(block = {
            val param = generateToggleBlockChatParam(messageId, blockActionType)
            val response = chatToggleBlockChat(param)
            val result = WrapperChatSetting(
                blockActionType = blockActionType,
                response = Success(response),
                element = element
            )
            _toggleBlock.value = result
        }, onError = {
            _toggleBlock.value = WrapperChatSetting(
                blockActionType = blockActionType,
                response = Fail(it)
            )
        })
    }

    private fun generateToggleBlockChatParam(
        messageId: String,
        blockActionType: ActionType
    ): ChatToggleBlockChatUseCase.Param {
        return ChatToggleBlockChatUseCase.Param().apply {
            this.msgId = messageId
            when (blockActionType) {
                BlockActionType.BlockChat -> {
                    this.blockType = ChatToggleBlockChatUseCase.BlockType.Personal.value
                    this.isBlocked = true
                }
                BlockActionType.UnblockChat -> {
                    this.blockType = ChatToggleBlockChatUseCase.BlockType.Personal.value
                    this.isBlocked = false
                }
                BlockActionType.BlockPromo -> {
                    this.blockType = ChatToggleBlockChatUseCase.BlockType.Promo.value
                    this.isBlocked = true
                }
                BlockActionType.UnblockPromo -> {
                    this.blockType = ChatToggleBlockChatUseCase.BlockType.Promo.value
                    this.isBlocked = false
                }
            }
        }
    }

    fun deleteChat(messageId: String) {
        launchCatchError(block = {
            val result = moveChatToTrashUseCase(messageId)
            if (result.chatMoveToTrash.list.isNotEmpty()) {
                val deletedChat = result.chatMoveToTrash.list.first()
                if (deletedChat.isSuccess == Constant.INT_STATUS_TRUE) {
                    _chatDeleteStatus.value = Success(result)
                } else {
                    val error = MessageErrorException(deletedChat.detailResponse)
                    _chatDeleteStatus.value = Fail(error)
                }
            }
        }, onError = {
            _chatDeleteStatus.value = Fail(it)
        })
    }

    fun getBackground() {
        launchCatchError(block = {
            getChatBackgroundUseCase(Unit).collect {
                _chatBackground.value = Success(it)
            }
        }, onError = {
            _chatBackground.value = Fail(it)
        })
    }

    fun loadAttachmentData(msgId: Long, chatRoom: ChatroomViewModel) {
        launchCatchError(block = {
            if (chatRoom.hasAttachment() && msgId != 0L) {
                val params = generateAttachmentParams(msgId, chatRoom.replyIDs)
                val result = chatAttachmentUseCase(params)
                val mapAttachment = chatAttachmentMapper.map(result)
                attachments.putAll(mapAttachment.toMap())
                _chatAttachments.value = attachments
            }
        }, onError = {
            val mapErrorAttachment = chatAttachmentMapper.mapError(chatRoom.replyIDs)
            attachments.putAll(mapErrorAttachment.toMap())
            _chatAttachments.value = attachments
        })
    }

    private fun generateAttachmentParams(
        msgId: Long,
        replyIDs: String
    ): ChatAttachmentUseCase.Param {
        val addressId = userLocationInfo.address_id.toLongOrZero()
        val districtId = userLocationInfo.district_id.toLongOrZero()
        val postalCode = userLocationInfo.postal_code
        val latlon = if (userLocationInfo.lat.isEmpty() || userLocationInfo.long.isEmpty()) {
            ""
        } else {
            "${userLocationInfo.lat},${userLocationInfo.long}"
        }
        return ChatAttachmentUseCase.Param(
            msgId = msgId,
            replyIDs = replyIDs,
            addressId = addressId,
            districtId = districtId,
            postalCode = postalCode,
            latlon = latlon
        )
    }

    fun getStickerGroupList(isSeller: Boolean) {
        launchCatchError(block = {
            getChatListGroupStickerUseCase(isSeller).collect {
                _chatListGroupSticker.value = Success(it)
            }
        }, onError = {
            _chatListGroupSticker.value = Fail(it)
        })
    }

    fun getSmartReplyWidget(msgId: String, productIds: String) {
        launchCatchError(block = {
            val param = GetSmartReplyQuestionUseCase.Param(
                msgId = msgId,
                productIds = productIds,
                addressId = userLocationInfo.address_id.toLongOrZero(),
                districtId = userLocationInfo.district_id.toLongOrZero(),
                postalCode = userLocationInfo.postal_code,
                latLon = userLocationInfo.latLong
            )
            chatSrwUseCase(param).collect {
                _srw.postValue(it)
            }
        }, onError = {
            _srw.postValue(Resource.error(it, null))
        })
    }

    fun adjustInterlocutorWarehouseId(msgId: String) {
        attachProductWarehouseId = "0"
        launchCatchError(block = {
            tokoNowWHUsecase(msgId).collect {
                attachProductWarehouseId = it.chatTokoNowWarehouse.warehouseId
            }
        },
            onError = {
                it.printStackTrace()
            })
    }

    fun addToWishList(
        productId: String,
        userId: String,
        wishlistActionListener: WishListActionListener
    ) {
        addWishListUseCase.createObservable(productId, userId, wishlistActionListener)
    }

    fun removeFromWishList(
        productId: String, userId: String, wishListActionListener: WishListActionListener
    ) {
        removeWishListUseCase.createObservable(productId, userId, wishListActionListener)
    }

    fun getExistingChat(messageId: String, isInit: Boolean = false) {
        if (messageId.isNotEmpty()) {
            launchCatchError(block = {
                val response = getChatUseCase.getFirstPageChat(messageId)
                val metaData = existingChatMapper.generateRoomMetaData(messageId, response)
                updateRoomMetaData(metaData)
                val chatroomViewModel = existingChatMapper.map(response)
                val result = GetChatResult(chatroomViewModel, response.chatReplies)
                _existingChat.value = Pair(Success(result), isInit)
            }, onError = {
                _existingChat.value = Pair(Fail(it), isInit)
            })
        }
    }

    private fun updateRoomMetaData(roomMetaData: RoomMetaData) {
        this.roomMetaData = roomMetaData
    }

    fun loadTopChat(messageId: String) {
        if (messageId.isNotEmpty()) {
            launchCatchError(block = {
                val response = getChatUseCase.getTopChat(messageId = messageId)
                val chatroomViewModel = existingChatMapper.map(response)
                val result = GetChatResult(chatroomViewModel, response.chatReplies)
                _topChat.value = Success(result)
            }, onError = {
                _topChat.value = Fail(it)
            })
        }
    }

    fun loadBottomChat(messageId: String) {
        if (messageId.isNotEmpty()) {
            launchCatchError(block = {
                val response = getChatUseCase.getBottomChat(messageId = messageId)
                val chatroomViewModel = existingChatMapper.map(response)
                val result = GetChatResult(chatroomViewModel, response.chatReplies)
                _bottomChat.value = Success(result)
            }, onError = {
                _bottomChat.value = Fail(it)
            })
        }
    }

    fun setBeforeReplyTime(createTime: String) {
        getChatUseCase.minReplyTime = createTime
    }

    fun isInTheMiddleOfThePage(): Boolean {
        return getChatUseCase.isInTheMiddleOfThePage()
    }

    fun resetChatUseCase() {
        getChatUseCase.reset()
    }


    fun deleteMsg(msgId: String, replyTimeNano: String) {
        launchCatchError(block = {
            val existingMessageIdParam = UnsendReplyUseCase.Param(
                msgID = msgId.toLongOrZero(),
                replyTimes = replyTimeNano
            )
            val response = unsendReplyUseCase(existingMessageIdParam)
            if (response.unsendReply.isSuccess) {
                _deleteBubble.value = Success(replyTimeNano)
            } else {
                _deleteBubble.value = Fail(IllegalStateException())
            }
        }, onError = {
            _deleteBubble.value = Fail(it)
        })
    }

    fun sendMsg(
        message: String,
        intention: String?,
        referredMsg: ParentReply?
    ) {
        val previewMsg = payloadGenerator.generatePreviewMsg(
            message = message,
            intention = intention,
            roomMetaData = roomMetaData,
            referredMsg = referredMsg
        )
        // TODO: implement list of attachments
        val wsPayload = payloadGenerator.generateWsPayload(
            message = message,
            intention = intention,
            roomMetaData = roomMetaData,
            previewMsg = previewMsg,
            attachments = listOf(), // products ?: attachmentsPreview
            userLocationInfo = userLocationInfo,
            referredMsg = referredMsg
        )
        showPreviewMsg(previewMsg)
        sendWsPayload(wsPayload)
        sendWsStopTyping()
    }

    private fun showPreviewMsg(previewMsg: SendableUiModel) {
        _previewMsg.postValue(previewMsg)
    }

    private fun sendWsStopTyping() {
        val wsPayload = payloadGenerator.generateWsPayloadStopTyping(
            roomMetaData.msgId
        )
        sendWsPayload(wsPayload)
    }

    private fun sendWsPayload(wsPayload: String) {
        chatWebSocket.sendPayload(wsPayload)
    }

    companion object {
        const val TAG = "TopchatWebSocketViewModel"
    }
}
package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.annotation.VisibleForTesting
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
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.ChatDeleteStatus
import com.tokopedia.topchat.chatroom.data.activityresult.UpdateProductStockResult
import com.tokopedia.topchat.chatroom.domain.mapper.ChatAttachmentMapper
import com.tokopedia.topchat.chatroom.domain.mapper.TopChatRoomGetExistingChatMapper
import com.tokopedia.topchat.chatroom.domain.pojo.GetChatResult
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.ActionType
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.BlockActionType
import com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings.WrapperChatSetting
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.ChatSmartReplyQuestionResponse
import com.tokopedia.topchat.chatroom.domain.pojo.srw.QuestionUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.ChatListGroupStickerResponse
import com.tokopedia.topchat.chatroom.domain.pojo.stickergroup.StickerGroup
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.domain.usecase.GetReminderTickerUseCase.Companion.FEATURE_ID_GENERAL
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.data.Resource
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.util.AddressUtil
import com.tokopedia.topchat.common.websocket.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.internal.toImmutableList
import javax.inject.Inject
import kotlin.collections.ArrayList

open class TopChatViewModel @Inject constructor(
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
    private var addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private var deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private var getChatUseCase: GetChatUseCase,
    private var unsendReplyUseCase: UnsendReplyUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val chatAttachmentMapper: ChatAttachmentMapper,
    private val existingChatMapper: TopChatRoomGetExistingChatMapper,
    private var getTemplateChatRoomUseCase: GetTemplateChatRoomUseCase,
    private var chatPreAttachPayload: GetChatPreAttachPayloadUseCase
) : BaseViewModel(dispatcher.main) {

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

    private val _tickerReminder = MutableLiveData<Result<ReminderTickerUiModel>>()
    val tickerReminder: LiveData<Result<ReminderTickerUiModel>>
        get() = _tickerReminder

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

    private val _chatAttachmentsPreview = MutableLiveData<ArrayMap<String, Attachment>>()
    val chatAttachmentsPreview: LiveData<ArrayMap<String, Attachment>>
        get() = _chatAttachmentsPreview

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

    private val _showableAttachmentPreviews = MutableLiveData<ArrayList<SendablePreview>>()
    val showableAttachmentPreviews: LiveData<ArrayList<SendablePreview>>
        get() = _showableAttachmentPreviews

    private val _templateChat = MutableLiveData<Result<ArrayList<Visitable<*>>>>()
    val templateChat: LiveData<Result<ArrayList<Visitable<*>>>>
        get() = _templateChat

    private val _userLocationInfo = MutableLiveData<LocalCacheModel?>()
    val userLocationInfo: LiveData<LocalCacheModel?>
        get() = _userLocationInfo

    private val _attachmentsPreview = MutableLiveData<ArrayList<SendablePreview>>()
    val attachmentsPreview: LiveData<ArrayList<SendablePreview>>
        get() = _attachmentsPreview

    private val _roomMetaData = MutableLiveData<RoomMetaData>()
    val roomMetaData: LiveData<RoomMetaData>
        get() = _roomMetaData

    var attachProductWarehouseId = "0"
    val attachments: ArrayMap<String, Attachment> = ArrayMap()
    val attachmentPreviewData: ArrayMap<String, Attachment> = ArrayMap()
    val onGoingStockUpdate: ArrayMap<String, UpdateProductStockResult> = ArrayMap()
    var pendingLoadProductPreview: ArrayList<String> = arrayListOf()

    init {
        _attachmentsPreview.value = arrayListOf()
        _roomMetaData.value = RoomMetaData()
    }

    fun initUserLocation(userLocation: LocalCacheModel?) {
        userLocation ?: return
        _userLocationInfo.value = userLocation
        this.attachProductWarehouseId = userLocation.warehouse_id
    }

    fun getMessageId(
        toUserId: String,
        toShopId: String,
        source: String
    ) {
        launchCatchError(block = {
            val existingMessageIdParam = GetExistingMessageIdUseCase.Param(
                toUserId = toUserId,
                toShopId = toShopId,
                source = source
            )
            val result = getExistingMessageIdUseCase(existingMessageIdParam)
            _messageId.value = Success(result.chatExistingChat.messageId)
            _roomMetaData.value?.updateMessageId(result.chatExistingChat.messageId)
        }, onError = {
                _messageId.value = Fail(it)
            })
    }

    fun getShopFollowingStatus(shopId: String) {
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
            productId = addToCartParam.productId,
            shopId = addToCartParam.shopId,
            quantity = addToCartParam.minOrder,
            atcFromExternalSource = AtcFromExternalSource.ATC_FROM_TOPCHAT,
            warehouseId = attachProductWarehouseId
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

    fun getTickerReminder(isSeller: Boolean) {
        launchCatchError(
            block = {
                val existingMessageIdParam = GetReminderTickerUseCase.Param(
                    featureId = FEATURE_ID_GENERAL,
                    isSeller = isSeller,
                    msgId = _roomMetaData.value?.msgId.toLongOrZero()
                )
                val result = reminderTickerUseCase(existingMessageIdParam)
                _tickerReminder.value = Success(result.getReminderTicker)
            },
            onError = { }
        )
    }

    fun closeTickerReminder(element: ReminderTickerUiModel, isSeller: Boolean) {
        launchCatchError(
            block = {
                val existingMessageIdParam = GetReminderTickerUseCase.Param(
                    featureId = element.featureId,
                    isSeller = isSeller,
                    msgId = _roomMetaData.value?.msgId.toLongOrZero()
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
                    warehouseId = attachProductWarehouseId,
                    // analytics data
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
        val addressId = _userLocationInfo.value?.address_id.toLongOrZero()
        val districtId = _userLocationInfo.value?.district_id.toLongOrZero()
        val postalCode = _userLocationInfo.value?.postal_code ?: ""
        val latlon = if (_userLocationInfo.value?.lat.isNullOrEmpty() || _userLocationInfo.value?.long.isNullOrEmpty()) {
            ""
        } else {
            "${_userLocationInfo.value?.lat},${_userLocationInfo.value?.long}"
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
            withTimeout(SRW_TIMEOUT) {
                val param = GetSmartReplyQuestionUseCase.Param(
                    msgId = msgId,
                    productIds = productIds,
                    addressId = _userLocationInfo.value?.address_id.toLongOrZero(),
                    districtId = _userLocationInfo.value?.district_id.toLongOrZero(),
                    postalCode = _userLocationInfo.value?.postal_code ?: "",
                    latLon = _userLocationInfo.value?.latLong ?: ""
                )
                chatSrwUseCase(param).collect {
                    _srw.postValue(it)
                }
            }
        }, onError = {
                _srw.postValue(Resource.error(it, null))
            })
    }

    fun adjustInterlocutorWarehouseId(msgId: String) {
        attachProductWarehouseId = "0"
        launchCatchError(
            block = {
                tokoNowWHUsecase(msgId).collect {
                    attachProductWarehouseId = it.chatTokoNowWarehouse.warehouseId
                }
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    fun addToWishListV2(
        productId: String,
        userId: String,
        wishlistActionListener: WishlistV2ActionListener
    ) {
        launch(dispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userId)
            val result = withContext(dispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistActionListener.onSuccessAddWishlist(result.data, productId)
            } else {
                val error = (result as Fail).throwable
                wishlistActionListener.onErrorAddWishList(error, productId)
            }
        }
    }

    fun removeFromWishListV2(
        productId: String,
        userId: String,
        wishListActionListener: WishlistV2ActionListener
    ) {
        launch(dispatcher.main) {
            deleteWishlistV2UseCase.setParams(productId, userId)
            val result = withContext(dispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishListActionListener.onSuccessRemoveWishlist(result.data, productId)
            } else {
                val error = (result as Fail).throwable
                wishListActionListener.onErrorRemoveWishlist(error, productId)
            }
        }
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
        _roomMetaData.value = roomMetaData
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
        return getChatUseCase.isInTheMiddleOfThePage().value ?: false
    }

    fun getMiddlePageLiveData(): LiveData<Boolean> {
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

    fun addAttachmentPreview(sendablePreview: SendablePreview) {
        _attachmentsPreview.value?.add(sendablePreview)
    }

    fun reloadCurrentAttachment() {
        val productIds = _attachmentsPreview.value?.mapNotNull {
            (it as? TopchatProductAttachmentPreviewUiModel)?.productId
        }
        productIds?.let {
            loadProductPreview(productIds)
        }
    }

    fun loadPendingProductPreview() {
        if (pendingLoadProductPreview.isEmpty()) return
        loadProductPreview(pendingLoadProductPreview.toImmutableList())
        pendingLoadProductPreview.clear()
    }

    fun loadProductPreview(productIds: List<String>) {
        if (productIds.isEmpty()) return
        if (_roomMetaData.value?.hasMsgId() == false) {
            pendingLoadProductPreview.clear()
            pendingLoadProductPreview.addAll(productIds)
            return
        }
        clearAttachmentPreview()
        launchCatchError(block = {
            showLoadingProductPreview(productIds)
            val param = GetChatPreAttachPayloadUseCase.Param(
                ids = productIds.joinToString(separator = ","),
                msgId = _roomMetaData.value?.msgId.toLongOrZero(),
                type = GetChatPreAttachPayloadUseCase.Param.TYPE_PRODUCT,
                addressID = _userLocationInfo.value?.address_id.toLongOrZero(),
                districtID = _userLocationInfo.value?.district_id.toLongOrZero(),
                postalCode = _userLocationInfo.value?.postal_code ?: "",
                latlon = _userLocationInfo.value?.latLong ?: ""
            )
            val response = chatPreAttachPayload(param)
            val mapAttachment = chatAttachmentMapper.map(response)
            attachmentPreviewData.putAll(mapAttachment.toMap())
            _chatAttachmentsPreview.value = attachmentPreviewData
        }, onError = {
                val errorMapAttachment = productIds.associateWith { ErrorAttachment() }
                attachmentPreviewData.putAll(errorMapAttachment)
                _chatAttachmentsPreview.value = attachmentPreviewData
            })
    }

    private fun showLoadingProductPreview(productIds: List<String>) {
        _roomMetaData.value?.let {
            val sendablePreviews: List<TopchatProductAttachmentPreviewUiModel> = productIds.map { productId ->
                val builder = TopchatProductAttachmentPreviewUiModel.Builder()
                    .withRoomMetaData(it)
                    .withProductId(productId)
                (builder as TopchatProductAttachmentPreviewUiModel.Builder).build()
            }
            _attachmentsPreview.value?.addAll(sendablePreviews)
            _showableAttachmentPreviews.value = ArrayList(sendablePreviews)
        }
    }

    fun isAttachmentPreviewReady(): Boolean {
        val sendable = _attachmentsPreview.value?.firstOrNull() as? DeferredAttachment
            ?: return !_attachmentsPreview.value.isNullOrEmpty()
        return !sendable.isLoading && !sendable.isError
    }

    fun clearAttachmentPreview() {
        _attachmentsPreview.value?.clear()
        attachmentPreviewData.clear()
    }

    fun removeAttachmentPreview(sendablePreview: SendablePreview) {
        _attachmentsPreview.value?.remove(sendablePreview)
    }

    fun initAttachmentPreview() {
        _showableAttachmentPreviews.value = _attachmentsPreview.value
    }

    fun getProductIdPreview(): List<String> {
        return _attachmentsPreview.value?.filterIsInstance<TopchatProductAttachmentPreviewUiModel>()
            ?.map { it.productId } ?: listOf()
    }

    fun hasEmptyAttachmentPreview(): Boolean {
        return _attachmentsPreview.value.isNullOrEmpty()
    }

    fun generateSrwQuestionUiModel(attachment: HeaderCtaButtonAttachment): QuestionUiModel {
        val addressMasking = AddressUtil.getAddressMasking(_userLocationInfo.value?.label ?: "")
        val ctaButton = attachment.ctaButton
        val productName = ctaButton.productName
        val srwMessage = "Ubah alamat pengiriman \"$productName\" ke $addressMasking"
        return QuestionUiModel(srwMessage, ctaButton.extras.intent)
    }

    fun getTemplate(isSeller: Boolean) {
        launchCatchError(block = {
            val result = getTemplateChatRoomUseCase.getTemplateChat(isSeller)
            val templateList = arrayListOf<Visitable<*>>()
            if (result.isEnabled) {
                templateList.addAll(result.listTemplate)
            }
            _templateChat.value = Success(templateList)
        }, onError = {
                _templateChat.value = Fail(it)
            })
    }

    fun addOngoingUpdateProductStock(
        productId: String,
        product: ProductAttachmentUiModel,
        adapterPosition: Int,
        parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        val result = UpdateProductStockResult(product, adapterPosition, parentMetaData)
        onGoingStockUpdate[productId] = result
    }

    fun updateMessageId(messageId: String) {
        _roomMetaData.value?.updateMessageId(messageId)
    }

    @VisibleForTesting
    fun setRoomMetaData(roomMetaData: RoomMetaData?) {
        _roomMetaData.value = roomMetaData
    }

    @VisibleForTesting
    fun setAttachmentsPreview(attachmentsPreview: ArrayList<SendablePreview>?) {
        _attachmentsPreview.value = attachmentsPreview
    }

    companion object {
        const val ENABLE_UPLOAD_IMAGE_SERVICE = "android_enable_topchat_upload_image_service"

        private const val SRW_TIMEOUT = 3000L
    }
}

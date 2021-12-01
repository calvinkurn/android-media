package com.tokopedia.topchat.chatroom.view.viewmodel

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.chat_common.data.ChatroomViewModel
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
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.ShopFollowingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.orderprogress.OrderProgressResponse
import com.tokopedia.topchat.chatroom.domain.pojo.param.AddToCartParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.ChatAttachmentParam
import com.tokopedia.topchat.chatroom.domain.pojo.param.ExistingMessageIdParam
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingResponse
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.topchat.chatroom.domain.usecase.GetReminderTickerUseCase.Param.Companion.SRW_TICKER
import com.tokopedia.topchat.common.Constant
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.common.network.TopchatCacheManager
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
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
    private val moveChatToTrashUseCase: MutationMoveChatToTrashUseCase,
    private val chatBackgroundUseCase: ChatBackgroundUseCaseNew,
    private val chatAttachmentUseCase: ChatAttachmentUseCaseNew,
    private val dispatcher: CoroutineDispatchers,
    private val remoteConfig: RemoteConfig,
    private val cacheManager: TopchatCacheManager,
    private val mapper: ChatAttachmentMapper
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

    private val _srwTickerReminder = MutableLiveData<Result<ReminderTickerUiModel>>()
    val srwTickerReminder: LiveData<Result<ReminderTickerUiModel>>
        get() = _srwTickerReminder

    private val _chatDeleteStatus = MutableLiveData<Result<ChatDeleteStatus>>()
    val chatDeleteStatus: LiveData<Result<ChatDeleteStatus>>
        get() = _chatDeleteStatus

    private val _chatBackground = MutableLiveData<Result<String>>()
    val chatBackground: MutableLiveData<Result<String>>
        get() = _chatBackground

    private val _chatAttachments = MutableLiveData<ArrayMap<String, Attachment>>()
    val chatAttachments: MutableLiveData<ArrayMap<String, Attachment>>
        get() = _chatAttachments

    val attachments: ArrayMap<String, Attachment> = ArrayMap()
    private var userLocationInfo = LocalCacheModel()
    var attachProductWarehouseId = "0"
        private set

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
        launchCatchError( block = {
            val existingMessageIdParam = ExistingMessageIdParam(
                toUserId = toUserId,
                toShopId = toShopId,
                source = source
            )
            val result = getExistingMessageIdUseCase(existingMessageIdParam)
            _messageId.value = Success(result.chatExistingChat.messageId)
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
            val cacheUrl = getCacheUrl(CHAT_BACKGROUND_CACHE_KEY)?.also {
                _chatBackground.value = Success(it)
            }
            val result = chatBackgroundUseCase(Unit)
            val responseImageUrl = result.chatBackground.urlImage
            if (responseImageUrl != cacheUrl) {
                _chatBackground.value = Success(responseImageUrl)
                cacheManager.saveCache(CHAT_BACKGROUND_CACHE_KEY, responseImageUrl)
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
                val mapAttachment = mapper.map(result)
                attachments.putAll(mapAttachment.toMap())
                _chatAttachments.value = attachments
            }
        }, onError = {
            val mapErrorAttachment = mapper.mapError(chatRoom.replyIDs)
            attachments.putAll(mapErrorAttachment.toMap())
            _chatAttachments.value = attachments
        })
    }

    private fun generateAttachmentParams(
        msgId: Long,
        replyIDs: String
    ): ChatAttachmentParam {
        val addressId = userLocationInfo.address_id.toLongOrZero()
        val districtId = userLocationInfo.district_id.toLongOrZero()
        val postalCode = userLocationInfo.postal_code
        val latlon = if (userLocationInfo.lat.isEmpty() || userLocationInfo.long.isEmpty()) {
            ""
        } else {
            "${userLocationInfo.lat},${userLocationInfo.long}"
        }
        return ChatAttachmentParam(
            msgId = msgId,
            replyIDs = replyIDs,
            addressId = addressId,
            districtId = districtId,
            postalCode = postalCode,
            latlon = latlon
        )
    }

    private fun getCacheUrl(cacheKey: String): String? {
        try {
            return cacheManager.loadCache(cacheKey, String::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private const val CHAT_BACKGROUND_CACHE_KEY = "cache_key_chat_background_url"
    }

}
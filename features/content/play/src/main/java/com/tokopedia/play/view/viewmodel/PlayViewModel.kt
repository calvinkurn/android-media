package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.domain.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playManager: TokopediaPlayManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        private val playSocket: PlaySocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    val observableVOD: LiveData<out ExoPlayer>
        get() = playManager.getObservableVideoPlayer()
    val observableGetChannelInfo: LiveData<Result<ChannelInfoUiModel>>
        get() = _observableGetChannelInfo
    val observableSocketInfo: LiveData<PlaySocketInfo>
        get() = _observableSocketInfo
    val observableVideoStream: LiveData<VideoStreamUiModel>
        get() = _observableVideoStream
    val observableNewChat: LiveData<PlayChatUiModel>
        get() = _observableNewChat
    val observableTotalLikes: LiveData<TotalLikeUiModel>
        get() = _observableTotalLikes
    val observableIsLikeContent: LiveData<Boolean>
        get() = _observableIsLikeContent
    val observableTotalViews: LiveData<TotalViewUiModel>
        get() = _observableTotalViews
    val observablePartnerInfo: LiveData<PartnerInfoUiModel>
        get() = _observablePartnerInfo
    val observableQuickReply: LiveData<QuickReplyUiModel>
        get() = _observableQuickReply
    val observableEvent: LiveData<EventUiModel>
        get() = _observableEvent
    val observableBottomInsetsState: LiveData<Map<BottomInsetsType, BottomInsetsState>>
        get() = _observableBottomInsetsState
    val observablePinned: LiveData<PinnedUiModel>
        get() = _observablePinned
    val observableVideoProperty: LiveData<VideoPropertyUiModel>
        get() = _observableVideoProperty
    val observableProductSheetContent: LiveData<ProductSheetUiModel>
        get() = _observableProductSheetContent
    val observableVariantSheetContent: LiveData<VariantSheetUiModel>
        get() = _observableVariantSheetContent
    val observableBadgeCart: LiveData<CartUiModel>
        get() = _observableBadgeCart

    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableVideoStream = MutableLiveData<VideoStreamUiModel>()
    private val _observableNewChat = MutableLiveData<PlayChatUiModel>()
    private val _observableTotalLikes = MutableLiveData<TotalLikeUiModel>()
    private val _observableIsLikeContent = MutableLiveData<Boolean>()
    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    private val _observablePartnerInfo: MutableLiveData<PartnerInfoUiModel> = MutableLiveData()
    private val _observableQuickReply = MutableLiveData<QuickReplyUiModel>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableProductSheetContent = MutableLiveData<ProductSheetUiModel>()
    private val _observableVariantSheetContent = MutableLiveData<VariantSheetUiModel>()
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observablePinned = MediatorLiveData<PinnedUiModel>()
    private val _observableBadgeCart = MutableLiveData<CartUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(observableVideoStream) {
            _observableVideoProperty.value = VideoPropertyUiModel(it.channelType, _observableVideoProperty.value?.state
                    ?: TokopediaPlayVideoState.NotConfigured)
        }
        addSource(playManager.getObservablePlayVideoState()) {
            _observableVideoProperty.value = VideoPropertyUiModel(_observableVideoProperty.value?.type ?: PlayChannelType.Unknown, it)
        }
        addSource(observablePartnerInfo) {
            val currentMessageValue = _observablePinnedMessage.value
            if (currentMessageValue != null) {
                _observablePinnedMessage.value = currentMessageValue.copy(
                        partnerName = it.name
                )
            }

            val currentProductValue = _observablePinnedProduct.value
            if (currentProductValue != null) {
                _observablePinnedProduct.value = currentProductValue.copy(
                        partnerName = it.name
                )
            }
        }
        addSource(observableProductSheetContent) {
            if (it.productList.isNullOrEmpty()) {
                val pinnedMessage = _observablePinnedMessage.value
                if (pinnedMessage != null) _observablePinnedMessage.value = _observablePinnedMessage.value
                else _observablePinned.value = PinnedRemoveUiModel
            }
            else _observablePinnedProduct.value = _observablePinnedProduct.value
        }
        addSource(observableEvent) {
            if (it.isFreeze) doOnChannelFreeze()
        }
    }

    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val stateHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    val isLive: PlayChannelType get() {
        val channelInfo = _observableGetChannelInfo.value
        return if (channelInfo != null && channelInfo is Success) {
            channelInfo.data.channelType
        } else {
            PlayChannelType.Unknown
        }
    }

    val contentId: Int get() {
        val channelInfo = _observableGetChannelInfo.value
        return if (channelInfo != null && channelInfo is Success) {
            channelInfo.data.contentId
        } else {
            0
        }
    }

    val contentType: Int get() {
        val channelInfo = _observableGetChannelInfo.value
        return if (channelInfo != null && channelInfo is Success) {
            channelInfo.data.contentType
        } else {
            0
        }
    }

    val likeType: Int get() {
        val channelInfo = _observableGetChannelInfo.value
        return if (channelInfo != null && channelInfo is Success) {
            channelInfo.data.likeType
        } else {
            0
        }
    }

    init {
        stateHandler.observeForever(stateHandlerObserver)

        _observablePinned.addSource(_observablePinnedMessage) {
            if (_observablePinnedProduct.value == null) {
                if (it == null) _observablePinned.value = PinnedRemoveUiModel
                else _observablePinned.value = it
            }
        }
        _observablePinned.addSource(_observablePinnedProduct) {
            if (_observableProductSheetContent.value?.productList.isNullOrEmpty() || it == null) {
                val pinnedMessage = _observablePinnedMessage.value
                if (pinnedMessage != null) _observablePinnedMessage.value = _observablePinnedMessage.value
                else _observablePinned.value = PinnedRemoveUiModel
            } else _observablePinned.value = it
        }

        _observableBottomInsetsState.value = getLatestBottomInsetsMapState()

//        startMockFreeze()
        setMockProductSheetContent()
//        setMockVariantSheetContent()
        setMockProductPinned()
    }

    override fun onCleared() {
        stateHandler.removeObserver(stateHandlerObserver)
        stopPlayer()
        super.onCleared()
    }

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
    }

    fun getDurationCurrentVideo(): Long {
        return playManager.getDurationVideo()
    }

    //region bottom insets
    fun onKeyboardShown(estimatedKeyboardHeight: Int) {
        val isLive = _observableVideoStream.value?.channelType?.isLive == true
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                if (isLive) BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedKeyboardHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == false
                ) else BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isShown == false
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onKeyboardHidden() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isShown == false
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowProductSheet(estimatedProductSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isHidden == false
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onHideProductSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isShown == false
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowVariantSheet(estimatedProductSheetHeight: Int, productId: String, action: ProductAction) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isHidden == false
                )

        _observableBottomInsetsState.value = insetsMap

        setMockVariantSheetContent(action)
    }

    fun onHideVariantSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isShown == false
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideAllInsets() {
        _observableBottomInsetsState.value = getDefaultBottomInsetsMapState()
    }
    //end region

    fun getChannelInfo(channelId: String) {

        var retryCount = 0

        fun getChannelInfoResponse(channelId: String): Job = launchCatchError(block = {
            val channel = withContext(dispatchers.io) {
                getChannelInfoUseCase.channelId = channelId
                return@withContext getChannelInfoUseCase.executeOnBackground()
            }

            launch { getTotalLikes(channel.contentId, channel.contentType, channel.likeType) }
            launch { getIsLike(channel.contentId, channel.contentType) }
            launch { getBadgeCart(channel.isShowCart) }

            /**
             * If Live => start web socket
             */
            if (channel.videoStream.isLive
                    && channel.videoStream.type.equals(PlayChannelType.Live.value, true))
                startWebSocket(channelId, channel.gcToken, channel.settings)

            playVideoStream(channel)

            val completeInfoUiModel = createCompleteInfoModel(channel)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableTotalViews.value = completeInfoUiModel.totalView
            _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
            _observableQuickReply.value = completeInfoUiModel.quickReply
            _observableVideoStream.value = completeInfoUiModel.videoStream
            _observableEvent.value = completeInfoUiModel.event
            _observablePartnerInfo.value = getPartnerInfo(completeInfoUiModel.channelInfo)
        }) {
            if (retryCount++ < MAX_RETRY_CHANNEL_INFO) getChannelInfoResponse(channelId)
            else if (it !is CancellationException) _observableGetChannelInfo.value = Fail(it)
        }

        getChannelInfoResponse(channelId)
    }

    fun resumeWithChannelId(channelId: String) {
        getChannelInfo(channelId)
    }

    fun destroy() {
        playSocket.destroy()
    }

    fun sendChat(message: String) {
        if (!userSession.isLoggedIn)
            return

        val cleanMessage = message.trimMultipleNewlines()
        playSocket.send(cleanMessage)
        _observableNewChat.value = mapPlayChat(
                PlayChat(
                        message = cleanMessage,
                        user = PlayChat.UserData(
                                id = userSession.userId,
                                name = userSession.name,
                                image = userSession.profilePicture)
                )
        )
    }

    fun changeLikeCount(shouldLike: Boolean) {
        val currentTotalLike = _observableTotalLikes.value ?: TotalLikeUiModel.empty()
        if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLike.totalLikeFormatted)) {
            var finalTotalLike = currentTotalLike.totalLike + (if (shouldLike) 1 else -1)
            if (finalTotalLike < 0) finalTotalLike = 0
            _observableTotalLikes.value = TotalLikeUiModel(
                    finalTotalLike,
                    finalTotalLike.toAmountString(amountStringStepArray, separator = ".")
            )
        }
    }

    private suspend fun getTotalLikes(contentId: Int, contentType: Int, likeType: Int) {
        try {
            val totalLike = withContext(dispatchers.io) {
                getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(contentId, contentType, likeType)
                getTotalLikeUseCase.executeOnBackground()
            }
            _observableTotalLikes.value = mapTotalLikes(totalLike)
        } catch (e: Exception) {}
    }

    private suspend fun getIsLike(contentId: Int, contentType: Int) {
        try {
            val isLiked = withContext(dispatchers.io) {
                getIsLikeUseCase.params = GetIsLikeUseCase.createParam(contentId, contentType)
                getIsLikeUseCase.executeOnBackground()
            }
            _observableIsLikeContent.value = isLiked
        } catch (e: Exception) {}
    }

    private suspend fun getPartnerInfo(channel: ChannelInfoUiModel): PartnerInfoUiModel {
        val partnerId = channel.partnerId
        return if (channel.partnerType == PartnerType.ADMIN) {
            PartnerInfoUiModel(
                    id = partnerId,
                    name = channel.moderatorName,
                    type = channel.partnerType,
                    isFollowed = true,
                    isFollowable = false
            )
        } else {
            val shopInfo = getPartnerInfo(partnerId, channel.partnerType)
            mapPartnerInfoFromShop(shopInfo)
        }
    }

    private suspend fun getPartnerInfo(partnerId: Long, partnerType: PartnerType) = withContext(dispatchers.io) {
            getPartnerInfoUseCase.params = GetPartnerInfoUseCase.createParam(partnerId.toInt(), partnerType)
            getPartnerInfoUseCase.executeOnBackground()
    }

    private suspend fun getBadgeCart(isShowCart: Boolean) {
        if (isShowCart) {
            try {
                val countCart = withContext(dispatchers.io) {
                    getCartCountUseCase.executeOnBackground()
                }
                _observableBadgeCart.value = CartUiModel(true, countCart)
            } catch (e: Exception) {}
        }
    }

    fun udpateBadgetCart() {
        val channelInfo = _observableGetChannelInfo.value
        if (channelInfo != null && channelInfo is Success) {
            launch { getBadgeCart(channelInfo.data.isShowCart) }
        }
    }

    private fun startWebSocket(channelId: String, gcToken: String, settings: Channel.Settings) {
        playSocket.channelId = channelId
        playSocket.gcToken = gcToken
        playSocket.settings = settings
        playSocket.connect(onMessageReceived = { response ->
            launch {
                val result = withContext(dispatchers.io) {
                    val socketMapper = PlaySocketMapper(response, amountStringStepArray)
                    socketMapper.mapping()
                }
                when (result) {
                    is TotalLike -> {
                        _observableTotalLikes.value = mapTotalLikes(result)
                    }
                    is TotalView -> {
                        _observableTotalViews.value = mapTotalViews(result)
                    }
                    is PlayChat -> {
                        _observableNewChat.value = mapPlayChat(result)
                    }
                    is PinnedMessage -> {
                        val partnerName = _observablePartnerInfo.value?.name.orEmpty()
                        _observablePinnedMessage.value = mapPinnedMessage(partnerName, result)
                    }
                    is QuickReply -> {
                        _observableQuickReply.value = mapQuickReply(result)
                    }
                    is BannedFreeze -> {
                        if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true)) {
                            _observableEvent.value = _observableEvent.value?.copy(
                                    isFreeze = result.isFreeze,
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true))
                        }
                    }
                }
            }
        }, onReconnect = {
            _observableSocketInfo.value = PlaySocketInfo.Reconnect
        }, onError = {
            _observableSocketInfo.value = PlaySocketInfo.Error(it)
            startWebSocket(channelId, gcToken, settings)
        })
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean) {
        playManager.safePlayVideoWithUriString(urlString, isLive)
    }

    private fun playVideoStream(channel: Channel) {
        if (channel.isActive) initiateVideo(channel)
    }

    private fun createCompleteInfoModel(channel: Channel) = PlayCompleteInfoUiModel(
            channelInfo = mapChannelInfo(channel),
            videoStream = mapVideoStream(channel.videoStream, channel.isActive),
            pinnedMessage = mapPinnedMessage(
                    _observablePartnerInfo.value?.name.orEmpty(),
                    channel.pinnedMessage
            ),
            pinnedProduct = mapPinnedProduct(_observablePartnerInfo.value?.name.orEmpty(), 2),
            quickReply = mapQuickReply(channel.quickReply),
            totalView = mapTotalViews(channel.totalViews),
            event = mapEvent(channel)
    )

    private fun mapChannelInfo(channel: Channel) = ChannelInfoUiModel(
            id = channel.channelId,
            title = channel.title,
            description = channel.description,
            channelType = if (channel.videoStream.isLive) PlayChannelType.Live else PlayChannelType.VOD,
            moderatorName = channel.moderatorName,
            partnerId = channel.partnerId,
            partnerType = PartnerType.getTypeByValue(channel.partnerType),
            contentId = channel.contentId,
            contentType = channel.contentType,
            likeType = channel.likeType,
            isShowCart = channel.isShowCart
    )

    private fun mapPinnedMessage(partnerName: String, pinnedMessage: PinnedMessage) = if (pinnedMessage.pinnedMessageId > 0 && pinnedMessage.title.isNotEmpty()) {
        PinnedMessageUiModel(
                applink = pinnedMessage.redirectUrl,
                partnerName = partnerName,
                title = pinnedMessage.title
        )
    } else null

    private fun mapPinnedProduct(partnerName: String, productCount: Int) = if (productCount > 0) PinnedProductUiModel(
            partnerName = partnerName,
            title = "",
            isPromo = true
    ) else null

    private fun mapVideoStream(videoStream: VideoStream, isActive: Boolean) = VideoStreamUiModel(
            uriString = videoStream.config.streamUrl,
            channelType = PlayChannelType.Live,
            isActive = isActive
    )

    private fun mapQuickReply(quickReplyList: List<String>) = QuickReplyUiModel(quickReplyList.filterNot { quickReply -> quickReply.isEmpty() || quickReply.isBlank() } )
    private fun mapQuickReply(quickReply: QuickReply) = mapQuickReply(quickReply.data)

    private fun mapTotalLikes(totalLike: Int, totalLikeString: String) = TotalLikeUiModel(totalLike, totalLikeString)
    private fun mapTotalLikes(totalLike: TotalLike) = mapTotalLikes(totalLike.totalLike, totalLike.totalLikeFormatted)

    private fun mapTotalViews(totalViewString: String) = TotalViewUiModel(totalViewString)
    private fun mapTotalViews(totalView: TotalView) = mapTotalViews(totalView.totalViewFormatted)

    private fun mapPlayChat(playChat: PlayChat) = PlayChatUiModel(
            messageId = playChat.messageId,
            userId = playChat.user.id,
            name = playChat.user.name,
            message = playChat.message,
            isSelfMessage = playChat.user.id == userSession.userId
    )

    private fun mapPartnerInfoFromShop(shopInfo: ShopInfo) = PartnerInfoUiModel(
            id = shopInfo.shopCore.shopId.toLong(),
            name = shopInfo.shopCore.name,
            type = PartnerType.SHOP,
            isFollowed = shopInfo.favoriteData.alreadyFavorited == 1,
            isFollowable = userSession.shopId != shopInfo.shopCore.shopId
    )

    private fun mapEvent(channel: Channel) = EventUiModel(
            isBanned = _observableEvent.value?.isBanned ?: false,
            isFreeze = !channel.isActive || channel.isFreeze,
            bannedMessage = channel.banned.message,
            bannedTitle = channel.banned.title,
            bannedButtonTitle = channel.banned.buttonTitle,
            freezeMessage = channel.freezeChannelState.desc,
            freezeTitle = channel.freezeChannelState.title,
            freezeButtonTitle = channel.freezeChannelState.btnTitle,
            freezeButtonUrl = channel.freezeChannelState.btnAppLink
    )

    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")

    private fun doOnChannelFreeze() {
        destroy()
        stopPlayer()
        onKeyboardHidden()
    }

    private fun stopPlayer() {
        playManager.stopPlayer()
    }

    private fun initiateVideo(channel: Channel) {
        startVideoWithUrlString(channel.videoStream.config.streamUrl, channel.videoStream.isLive)
        playManager.setRepeatMode(false)
    }

    private fun getLatestBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentValue = _observableBottomInsetsState.value ?: return getDefaultBottomInsetsMapState()
        currentValue.values.forEach { it.isPreviousStateSame = true }

        return currentValue
    }

    private fun getDefaultBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> = mapOf(
            BottomInsetsType.Keyboard to BottomInsetsState.Hidden(false),
            BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(false),
            BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(false)
    )

    companion object {
        private const val MAX_RETRY_CHANNEL_INFO = 3
    }

    private fun startMockFreeze() {
        launch(dispatchers.io) {
            delay(10000)
            withContext(dispatchers.main) {
                _observableEvent.value = _observableEvent.value?.copy(
                        isFreeze = true
                )
            }
        }
    }

    private fun setMockProductSheetContent() {
        launch(dispatchers.io) {
            delay(3000)
            withContext(dispatchers.main) {
                _observableProductSheetContent.value = ProductSheetUiModel(
                        title = "Barang & Promo Pilihan",
                        voucherList = List(5) { voucherIndex ->
                            MerchantVoucherUiModel(
                                    type = if (voucherIndex % 2 == 0) MerchantVoucherType.Discount else MerchantVoucherType.Shipping,
                                    title = if (voucherIndex % 2 == 0) "Cashback ${(voucherIndex + 1) * 2}rb" else "Gratis ongkir ${(voucherIndex + 1) * 2}rb",
                                    description = "min. pembelian ${(voucherIndex + 1)}00rb"
                            )
                        },
                        productList = List(5) {
                            ProductLineUiModel(
                                    id = it.toString(),
                                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
                                    title = "Product $it",
                                    price = if (it % 2 == 0) {
                                        OriginalPrice("Rp20$it.000")
                                    } else {
                                        DiscountedPrice(
                                                originalPrice = "Rp20$it.000",
                                                discountPercent = it * 10,
                                                discountedPrice = "Rp2$it.000"
                                        )
                                    }
                            )
                        }
                )
            }
        }
    }

    private fun setMockVariantSheetContent(action: ProductAction) {
        _observableVariantSheetContent.value = VariantSheetUiModel(
                title = "Detail Produk",
                productId = "123",
                action = action
        )
    }

    private fun setMockProductPinned() {
        launch(dispatchers.io) {
            delay(3000)
            withContext(dispatchers.main) {
                _observablePinnedProduct.value = PinnedProductUiModel(
                        partnerName = "GSK Official Store",
                        title = "Ayo belanja barang pilihan kami sebelum kehabisan!",
                        isPromo = true
                )
            }
        }
    }
}
package com.tokopedia.play.view.viewmodel

import android.net.Uri
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
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.player.PlayVideoManager
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
        private val playVideoManager: PlayVideoManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        private val getProductTagItemsUseCase: GetProductTagItemsUseCase,
        private val playSocket: PlaySocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    val observableVOD: LiveData<out ExoPlayer>
        get() = playVideoManager.getObservableVideoPlayer()

    /**
     * Not Used for Event to component
     */
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
    val observableProductSheetContent: LiveData<PlayResult<ProductSheetUiModel>>
        get() = _observableProductSheetContent
    val observableBadgeCart: LiveData<CartUiModel>
        get() = _observableBadgeCart
    val channelType: PlayChannelType
        get() {
            val channelInfo = _observableGetChannelInfo.value
            return if (channelInfo != null && channelInfo is Success) {
                channelInfo.data.channelType
            } else {
                PlayChannelType.Unknown
            }
        }
    val contentId: Int
        get() {
            val channelInfo = _observableGetChannelInfo.value
            return if (channelInfo != null && channelInfo is Success) {
                channelInfo.data.contentId
            } else {
                0
            }
        }
    val contentType: Int
        get() {
            val channelInfo = _observableGetChannelInfo.value
            return if (channelInfo != null && channelInfo is Success) {
                channelInfo.data.contentType
            } else {
                0
            }
        }
    val likeType: Int
        get() {
            val channelInfo = _observableGetChannelInfo.value
            return if (channelInfo != null && channelInfo is Success) {
                channelInfo.data.likeType
            } else {
                0
            }
        }
    val partnerId: Long?
        get() = _observablePartnerInfo.value?.id
    val totalView: String?
        get() = _observableTotalViews.value?.totalView

    val stateHelper: StateHelperUiModel
        get() {
            val pinned = _observablePinned.value
            val videoStream = _observableVideoStream.value
            val bottomInsets = _observableBottomInsetsState.value
            return StateHelperUiModel(
                    shouldShowPinnedMessage = pinned is PinnedMessageUiModel || pinned is PinnedProductUiModel,
                    channelType = videoStream?.channelType ?: PlayChannelType.Unknown,
                    bottomInsets = bottomInsets ?: getDefaultBottomInsetsMapState()
            )
        }

    private val isProductSheetInitialized: Boolean
        get() = _observableProductSheetContent.value != null

    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableVideoStream = MutableLiveData<VideoStreamUiModel>()
    private val _observableNewChat = MutableLiveData<PlayChatUiModel>()
    private val _observableTotalLikes = MutableLiveData<TotalLikeUiModel>()
    private val _observableIsLikeContent = MutableLiveData<Boolean>()
    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    private val _observablePartnerInfo = MutableLiveData<PartnerInfoUiModel>()
    private val _observableQuickReply = MutableLiveData<QuickReplyUiModel>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observablePinnedProduct = MutableLiveData<PinnedProductUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val _observableProductSheetContent = MutableLiveData<PlayResult<ProductSheetUiModel>>()
    private val _observableBottomInsetsState = MutableLiveData<Map<BottomInsetsType, BottomInsetsState>>()
    private val _observablePinned = MediatorLiveData<PinnedUiModel>()
    private val _observableBadgeCart = MutableLiveData<CartUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(playVideoManager.getObservablePlayVideoState()) {
            _observableVideoProperty.value = VideoPropertyUiModel(it)
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
            if (it is PlayResult.Success && it.data.productList.isNullOrEmpty()) {
                val pinnedMessage = _observablePinnedMessage.value
                if (pinnedMessage != null) _observablePinnedMessage.value = _observablePinnedMessage.value
                else _observablePinned.value = PinnedRemoveUiModel
            } else _observablePinnedProduct.value = _observablePinnedProduct.value
        }
        addSource(observableEvent) {
            if (it.isFreeze || it.isBanned) doOnFreezeBan()
        }
    }

    //region helper
    private val hasWordsOrDotsRegex = Regex("(\\.+|[a-z]+)")
    private val amountStringStepArray = arrayOf("k", "m")
    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
    //endregion

    /**
     * DO NOT CHANGE THIS TO LAMBDA
     */
    private val stateHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    init {
        stateHandler.observeForever(stateHandlerObserver)

        _observablePinned.addSource(_observablePinnedMessage) {
            if (_observablePinnedProduct.value == null) {
                if (it == null) _observablePinned.value = PinnedRemoveUiModel
                else if (_observablePinned.value != it) _observablePinned.value = it
            }
        }
        _observablePinned.addSource(_observablePinnedProduct) {
            val productSheet = _observableProductSheetContent.value
            if (productSheet is PlayResult.Success && productSheet.data.productList.isNotEmpty() && it != null) {
                if (_observablePinned.value != it) _observablePinned.value = it
            } else {
                val pinnedMessage = _observablePinnedMessage.value
                if (pinnedMessage != null) _observablePinnedMessage.value = _observablePinnedMessage.value
                else _observablePinned.value = PinnedRemoveUiModel
            }
        }

        _observableBottomInsetsState.value = getLatestBottomInsetsMapState()

//        startMockFreeze()
//        setMockProductSocket()
//        setMockVoucherSocket()
//        setMockProductSheetContent()
//        setMockVariantSheetContent()
//        setMockProductPinned()
    }

    //region lifecycle
    fun resumeWithChannelId(channelId: String) {
        getChannelInfo(channelId)
    }

    override fun onCleared() {
        super.onCleared()
        stateHandler.removeObserver(stateHandlerObserver)
        destroy()
        stopPlayer()
    }

    override fun flush() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.cancelChildren()
        }
    }
    //endregion

    //region bottom insets
    fun onKeyboardShown(estimatedKeyboardHeight: Int) {
        val isLive = channelType.isLive
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                if (isLive) BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedKeyboardHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isShown == true
                ) else BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onKeyboardHidden() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.Keyboard] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.Keyboard]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowProductSheet(estimatedProductSheetHeight: Int) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isShown == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onHideProductSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.ProductSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.ProductSheet]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun onShowVariantSheet(estimatedProductSheetHeight: Int, product: ProductLineUiModel, action: ProductAction) {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Shown(
                        estimatedInsetsHeight = estimatedProductSheetHeight,
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isShown == true
                )

        _observableBottomInsetsState.value = insetsMap
//        setMockVariantSheetContent(action)
    }

    fun onHideVariantSheet() {
        val insetsMap = getLatestBottomInsetsMapState().toMutableMap()

        insetsMap[BottomInsetsType.VariantSheet] =
                BottomInsetsState.Hidden(
                        isPreviousStateSame = insetsMap[BottomInsetsType.VariantSheet]?.isHidden == true
                )

        _observableBottomInsetsState.value = insetsMap
    }

    fun hideInsets(isKeyboardHandled: Boolean) {
        val defaultBottomInsets = getDefaultBottomInsetsMapState()
        _observableBottomInsetsState.value = if (isKeyboardHandled) {
            defaultBottomInsets.toMutableMap().apply {
                this[BottomInsetsType.Keyboard] = BottomInsetsState.Hidden(true)
            }
        } else defaultBottomInsets
    }

    private fun getLatestBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentValue = _observableBottomInsetsState.value
                ?: return getDefaultBottomInsetsMapState()
        currentValue.values.forEach {
            it.isPreviousStateSame = true
            if (it is BottomInsetsState.Shown) it.deepLevel += 1
        }

        return currentValue
    }

    private fun getDefaultBottomInsetsMapState(): Map<BottomInsetsType, BottomInsetsState> {
        val currentBottomInsetsMap = _observableBottomInsetsState.value
        val defaultKeyboardState = currentBottomInsetsMap?.get(BottomInsetsType.Keyboard)?.isHidden ?: false
        val defaultProductSheetState = currentBottomInsetsMap?.get(BottomInsetsType.ProductSheet)?.isHidden ?: false
        val defaultVariantSheetState = currentBottomInsetsMap?.get(BottomInsetsType.VariantSheet)?.isHidden ?: false
        return mapOf(
                BottomInsetsType.Keyboard to BottomInsetsState.Hidden(defaultKeyboardState),
                BottomInsetsType.ProductSheet to BottomInsetsState.Hidden(defaultProductSheetState),
                BottomInsetsType.VariantSheet to BottomInsetsState.Hidden(defaultVariantSheetState)
        )
    }
    //endregion

    //region video player
    fun startCurrentVideo() {
        playVideoManager.resumeCurrentVideo()
    }

    fun getDurationCurrentVideo(): Long {
        return playVideoManager.getDurationVideo()
    }

    private fun initiateVideo(channel: Channel) {
        startVideoWithUrlString(
                channel.videoStream.config.streamUrl,
                channel.videoStream.isLive,
                bufferControl = channel.videoStream.bufferControl?.let { mapBufferControl(it) }
                        ?: PlayBufferControl()
        )
        playVideoManager.setRepeatMode(false)
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean, bufferControl: PlayBufferControl) {
        playVideoManager.safePlayVideoWithUri(Uri.parse(urlString), isLive, bufferControl)
    }

    private fun playVideoStream(channel: Channel) {
        if (channel.isActive) initiateVideo(channel)
    }

    private fun stopPlayer() {
        playVideoManager.stopPlayer()
    }
    //endregion

    //region API & Socket
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
            launch { if (channel.isShowProductTagging) getProductTagItems(channel) }

            startWebSocket(channelId, channel.gcToken, channel.settings)

            playVideoStream(channel)

            val completeInfoUiModel = createCompleteInfoModel(channel)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableTotalViews.value = completeInfoUiModel.totalView
            _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
            _observablePinnedProduct.value = completeInfoUiModel.pinnedProduct
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

    fun sendChat(message: String) {
        if (!userSession.isLoggedIn)
            return

        val cleanMessage = message.trimMultipleNewlines()
        playSocket.send(cleanMessage)
        _observableNewChat.value = PlayUiMapper.mapPlayChat(userSession.userId,
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

    fun onBackPressed(): Boolean {
        val shownBottomSheets = observableBottomInsetsState.value
                ?.filter { it.value.isShown }
                ?.mapValues { it.value as BottomInsetsState.Shown }
                .orEmpty()
        val entry = shownBottomSheets.minBy { it.value.deepLevel }
        when (entry?.key) {
            BottomInsetsType.Keyboard -> onKeyboardHidden()
            BottomInsetsType.ProductSheet -> onHideProductSheet()
            BottomInsetsType.VariantSheet -> onHideVariantSheet()
        }
        return shownBottomSheets.isNotEmpty()
    }

    private fun destroy() {
        playSocket.destroy()
    }

    private suspend fun getTotalLikes(contentId: Int, contentType: Int, likeType: Int) {
        try {
            val totalLike = withContext(dispatchers.io) {
                getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(contentId, contentType, likeType)
                getTotalLikeUseCase.executeOnBackground()
            }
            _observableTotalLikes.value = PlayUiMapper.mapTotalLikes(totalLike)
        } catch (e: Exception) {
        }
    }

    private suspend fun getIsLike(contentId: Int, contentType: Int) {
        try {
            val isLiked = withContext(dispatchers.io) {
                getIsLikeUseCase.params = GetIsLikeUseCase.createParam(contentId, contentType)
                getIsLikeUseCase.executeOnBackground()
            }
            _observableIsLikeContent.value = isLiked
        } catch (e: Exception) {
        }
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
            PlayUiMapper.mapPartnerInfoFromShop(userSession.shopId, shopInfo)
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
                _observableBadgeCart.value = CartUiModel(isShowCart, countCart)
            } catch (e: Exception) {
            }
        }
    }

    private fun getProductTagItems(channel: Channel) {
        if (!isProductSheetInitialized) _observableProductSheetContent.value = PlayResult.Loading(
                showPlaceholder = true
        )

        launchCatchError(block = {
            val productTagsItems = withContext(dispatchers.io) {
                getProductTagItemsUseCase.params = GetProductTagItemsUseCase.createParam(channel.channelId)
                getProductTagItemsUseCase.executeOnBackground()
            }
            _observableProductSheetContent.value = PlayResult.Success(
                    PlayUiMapper.mapProductSheet(
                            channel.pinnedProduct.titleBottomSheet,
                            productTagsItems)
            )
        }) {
            _observableProductSheetContent.value = PlayResult.Failure(it) {
                getProductTagItems(channel)
            }
        }
    }

    fun updateBadgeCart() {
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
                    val socketMapper = PlaySocketMapper(response)
                    socketMapper.mapping()
                }
                when (result) {
                    is TotalLike -> {
                        _observableTotalLikes.value = PlayUiMapper.mapTotalLikes(result)
                    }
                    is TotalView -> {
                        _observableTotalViews.value = PlayUiMapper.mapTotalViews(result)
                    }
                    is PlayChat -> {
                        _observableNewChat.value = PlayUiMapper.mapPlayChat(userSession.userId, result)
                    }
                    is PinnedMessage -> {
                        val partnerName = _observablePartnerInfo.value?.name.orEmpty()
                        _observablePinnedMessage.value = PlayUiMapper.mapPinnedMessage(partnerName, result)
                    }
                    is QuickReply -> {
                        _observableQuickReply.value = PlayUiMapper.mapQuickReply(result)
                    }
                    is BannedFreeze -> {
                        if (result.channelId.isNotEmpty() && result.channelId.equals(channelId, true)) {
                            _observableEvent.value = _observableEvent.value?.copy(
                                    isFreeze = result.isFreeze,
                                    isBanned = result.isBanned && result.userId.isNotEmpty()
                                            && result.userId.equals(userSession.userId, true))
                        }
                    }
                    is ProductTag -> {
                        val productSheet = _observableProductSheetContent.value
                        val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                        _observableProductSheetContent.value = PlayResult.Success(
                                data = currentProduct.copy(productList = PlayUiMapper.mapItemProducts(result.listOfProducts))
                        )
                    }
                    is MerchantVoucher -> {
                        val productSheet = _observableProductSheetContent.value
                        val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                        _observableProductSheetContent.value = PlayResult.Success(
                                data = currentProduct.copy(voucherList = PlayUiMapper.mapItemVouchers(result.listOfVouchers))
                        )
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

    private fun createCompleteInfoModel(channel: Channel) = PlayCompleteInfoUiModel(
            channelInfo = PlayUiMapper.mapChannelInfo(channel),
            videoStream = PlayUiMapper.mapVideoStream(channel.videoStream, channel.isActive),
            pinnedMessage = PlayUiMapper.mapPinnedMessage(
                    _observablePartnerInfo.value?.name.orEmpty(),
                    channel.pinnedMessage
            ),
            pinnedProduct = PlayUiMapper.mapPinnedProduct(
                    _observablePartnerInfo.value?.name.orEmpty(),
                    channel.isShowProductTagging,
                    channel.pinnedProduct),
            quickReply = PlayUiMapper.mapQuickReply(channel.quickReply),
            totalView = PlayUiMapper.mapTotalViews(channel.totalViews),
            event = mapEvent(channel)
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

    private fun mapBufferControl(bufferControl: VideoStream.BufferControl) = PlayBufferControl(
            minBufferMs = bufferControl.minBufferingSecond * MS_PER_SECOND,
            maxBufferMs = bufferControl.maxBufferingSecond * MS_PER_SECOND,
            bufferForPlaybackMs = bufferControl.bufferForPlayback * MS_PER_SECOND,
            bufferForPlaybackAfterRebufferMs = bufferControl.bufferForPlaybackAfterRebuffer * MS_PER_SECOND
    )

    private fun doOnFreezeBan() {
        destroy()
        stopPlayer()
        hideInsets(isKeyboardHandled = false)
    }
    //endregion

    companion object {
        private const val MAX_RETRY_CHANNEL_INFO = 3

        private const val MS_PER_SECOND = 1000
    }


    //region mock
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

    private fun setMockProductSocket() {
        launch(dispatchers.io) {
            delay(10000)
            withContext(dispatchers.main) {
                val productSheet = _observableProductSheetContent.value
                val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                _observableProductSheetContent.value = PlayResult.Success(currentProduct.copy(
                        productList = List(5) {
                            ProductLineUiModel(
                                    id = it.toString(),
                                    shopId = "123",
                                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
                                    title = "Product $it",
                                    isVariantAvailable = true,
                                    price = if (it % 2 == 0) {
                                        OriginalPrice("Rp20$it.000", 20000)
                                    } else {
                                        DiscountedPrice(
                                                originalPrice = "Rp20$it.000",
                                                discountPercent = it * 10,
                                                discountedPrice = "Rp2$it.000",
                                                discountedPriceNumber = 20000
                                        )
                                    },
                                    stock = if (it % 2 == 0) {
                                        OutOfStock
                                    } else {
                                        StockAvailable(it * 10)
                                    },
                                    minQty = 2,
                                    isFreeShipping = true,
                                    applink = null
                            )
                        }
                ))
            }
        }
    }

    private fun setMockVoucherSocket() {
        launch(dispatchers.io) {
            delay(15000)
            withContext(dispatchers.main) {
                val productSheet = _observableProductSheetContent.value
                val currentProduct = if (productSheet is PlayResult.Success) productSheet.data else ProductSheetUiModel.empty()
                _observableProductSheetContent.value = PlayResult.Success(currentProduct.copy(
                        voucherList = List(5) { voucherIndex ->
                            MerchantVoucherUiModel(
                                    type = if (voucherIndex % 2 == 0) MerchantVoucherType.Discount else MerchantVoucherType.Shipping,
                                    title = if (voucherIndex % 2 == 0) "Cashback ${(voucherIndex + 1) * 2}rb" else "Gratis ongkir ${(voucherIndex + 1) * 2}rb",
                                    description = "min. pembelian ${(voucherIndex + 1)}00rb"
                            )
                        }
                ))
            }
        }
    }

    private fun setMockProductSheetContent() {
        launch(dispatchers.io) {
            delay(3000)
            withContext(dispatchers.main) {
                _observableProductSheetContent.value = PlayResult.Success(ProductSheetUiModel(
                        title = "Barang & Promo Pilihan",
                        voucherList = List(5) { voucherIndex ->
                                                        MerchantVoucherUiModel(
                                    type = if (voucherIndex % 2 == 0) MerchantVoucherType.Discount else MerchantVoucherType.Shipping,
                                    title = if (voucherIndex % 2 == 0) "Cashback ${(voucherIndex + 1) * 2}rb" else "Gratis ongkir ${(voucherIndex + 1) * 2}rb",
                                    description = "min. pembelian ${(voucherIndex + 1)}00rb"
                            )
//                            VoucherPlaceholderUiModel
                        },
                        productList = List(5) {
                            ProductLineUiModel(
                                    id = "697897875",
                                    shopId = "123",
                                    imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
                                    title = "Product $it",
                                    isVariantAvailable = true,
                                    price = if (it % 2 == 0) {
                                        OriginalPrice("Rp20$it.000", 20000)
                                    } else {
                                        DiscountedPrice(
                                                originalPrice = "Rp20$it.000",
                                                discountPercent = it * 10,
                                                discountedPrice = "Rp2$it.000",
                                                discountedPriceNumber = 20000
                                        )
                                    },
                                    stock = if (it % 2 == 0) {
                                        OutOfStock
                                    } else {
                                        StockAvailable(it * 10)
                                    },
                                    minQty = 2,
                                    isFreeShipping = true,
                                    applink = "tokopedia://login"
                            )
//                            ProductPlaceholderUiModel
                        }
                ))
            }
        }
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
    //endregion
}
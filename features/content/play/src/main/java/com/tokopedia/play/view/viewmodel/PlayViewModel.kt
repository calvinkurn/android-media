package com.tokopedia.play.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toAmountString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.domain.GetIsLikeUseCase
import com.tokopedia.play.domain.GetPartnerInfoUseCase
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.KeyboardState
import com.tokopedia.play.view.type.PlayChannelType
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
        private val playSocket: PlaySocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    val observableVOD: LiveData<out ExoPlayer>
        get() = playManager.getObservableVideoPlayer()
    val observableGetChannelInfo: LiveData<Result<ChannelInfoUiModel>>
        get() = _observableGetChannelInfo
    val observableVideoStream: LiveData<VideoStreamUiModel>
        get() = _observableVideoStream
    val observableSocketInfo: LiveData<PlaySocketInfo>
        get() = _observableSocketInfo
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
    val observableKeyboardState: LiveData<KeyboardState>
        get() = _observableKeyboardState
    val observablePinnedMessage: LiveData<PinnedMessageUiModel>
        get() = _observablePinnedMessage
    val observableVideoProperty: LiveData<VideoPropertyUiModel>
        get() = _observableVideoProperty

    private val _observableVOD = MutableLiveData<ExoPlayer>()
    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    private val _observableChanelInfo = MutableLiveData<ChannelInfoUiModel>()
    private val _observableVideoStream = MutableLiveData<VideoStreamUiModel>()
    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    private val _observableNewChat = MutableLiveData<PlayChatUiModel>()
    private val _observableTotalLikes = MutableLiveData<TotalLikeUiModel>()
    private val _observableIsLikeContent = MutableLiveData<Boolean>()
    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    private val _observablePartnerInfo: MutableLiveData<PartnerInfoUiModel> = MutableLiveData()
    private val _observableQuickReply = MutableLiveData<QuickReplyUiModel>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observableKeyboardState = MutableLiveData<KeyboardState>()
    private val _observablePinnedMessage = MutableLiveData<PinnedMessageUiModel>()
    private val _observableVideoProperty = MutableLiveData<VideoPropertyUiModel>()
    private val stateHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(observableVideoStream) {
            _observableVideoProperty.value = VideoPropertyUiModel(it.channelType, _observableVideoProperty.value?.state
                    ?: TokopediaPlayVideoState.NotConfigured)
        }
        addSource(playManager.getObservablePlayVideoState()) {
            _observableVideoProperty.value = VideoPropertyUiModel(_observableVideoProperty.value?.type ?: PlayChannelType.Unknown, it)
        }
        addSource(observablePartnerInfo) {
            val currentValue = _observablePinnedMessage.value
            if (currentValue != null) _observablePinnedMessage.value = currentValue.copy(
                    partnerName = it.name
            )
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

    val isLive: Boolean
        get() = _observableChanelInfo.value?.isLive ?: false
    val contentId: Int
        get() = _observableChanelInfo.value?.contentId.toZeroIfNull()
    val contentType: Int
        get() = _observableChanelInfo.value?.contentType.toZeroIfNull()
    val likeType: Int
        get() = _observableChanelInfo.value?.likeType.toZeroIfNull()

    init {
        //TODO(Remove, ONLY FOR TESTING)
//        initMockChat()
//        initMockFreeze()
//        initMockBanned()

        _observableVOD.value = playManager.videoPlayer
        stateHandler.observeForever(stateHandlerObserver)
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

    fun onKeyboardShown(estimatedKeyboardHeight: Int) {
        _observableKeyboardState.value =
                if (_observableVideoStream.value?.channelType?.isLive == true) KeyboardState.Shown(estimatedKeyboardHeight, _observableKeyboardState.value?.isHidden == false)
                else KeyboardState.Hidden(observableKeyboardState.value?.isShown == false)
    }

    fun onKeyboardHidden() {
        _observableKeyboardState.value = KeyboardState.Hidden(observableKeyboardState.value?.isShown == false)
    }

    fun getChannelInfo(channelId: String) {

        var retryCount = 0

        fun getChannelInfoResponse(channelId: String): Job = launchCatchError(block = {
            val channel = withContext(dispatchers.io) {
                getChannelInfoUseCase.channelId = channelId
                return@withContext getChannelInfoUseCase.executeOnBackground()
            }

            launch { getTotalLikes(channel.contentId, channel.contentType) }
            launch { getIsLike(channel.contentId, channel.contentType) }

            // TODO("remove, for testing")
//            channel.videoStream = VideoStream(
//                    "vertical",
//                    "live",
//                    true,
//                    VideoStream.Config(streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))

            /**
             * If Live => start web socket
             */
            if (channel.videoStream.isLive
                    && channel.videoStream.type.equals(PlayChannelType.Live.value, true))
                startWebSocket(channelId, channel.gcToken, channel.settings)

            playVideoStream(channel)

            val completeInfoUiModel = createCompleteInfoModel(channel)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableChanelInfo.value = completeInfoUiModel.channelInfo
            _observableTotalViews.value = completeInfoUiModel.totalView
            _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
            _observableQuickReply.value = completeInfoUiModel.quickReply
            _observableVideoStream.value = completeInfoUiModel.videoStream
            _observableEvent.value = mapEvent(channel)
            _observablePartnerInfo.value = getPartnerInfo(completeInfoUiModel.channelInfo)
        }) {
            if (retryCount++ < MAX_RETRY_CHANNEL_INFO) getChannelInfoResponse(channelId)
            else if (it !is CancellationException) _observableGetChannelInfo.value = Fail(it)
        }

        getChannelInfoResponse(channelId)
    }

    fun resume() {
        checkIsFollowedShop()
    }

    fun destroy() {
        playSocket.destroy()
    }

    fun sendChat(message: String) {
        if (!userSession.isLoggedIn)
            return

        val cleanMessage = message.trimMultipleNewlines()
        playSocket.send(cleanMessage, onSuccess = {
            _observableNewChat.value = mapPlayChat(
                    PlayChat(
                            message = cleanMessage,
                            user = PlayChat.UserData(
                                    id = userSession.userId,
                                    name = userSession.name,
                                    image = userSession.profilePicture)
                    )
            )
        })
    }

    fun changeLikeCount(shouldLike: Boolean) {
        val currentTotalLike = _observableTotalLikes.value ?: TotalLikeUiModel.empty()
        if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLike.totalLikeFormatted)) {
            var finalTotalLike = currentTotalLike.totalLike + (if (shouldLike) 1 else -1)
            if (finalTotalLike < 0) finalTotalLike = 0
            _observableTotalLikes.value = TotalLikeUiModel(
                    finalTotalLike,
                    finalTotalLike.toAmountString(amountStringStepArray)
            )
        }
    }

    private suspend fun getTotalLikes(contentId: Int, contentType: Int) {
        try {
            val totalLike = withContext(dispatchers.io) {
                getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(contentId, contentType, isLive)
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
        val partnerType = PartnerType.getTypeByValue(channel.partnerType)
        val partnerId = channel.partnerId
        return if (partnerType == PartnerType.ADMIN) {
            PartnerInfoUiModel(
                    id = partnerId,
                    name = channel.moderatorName,
                    type = partnerType,
                    isFollowed = true,
                    isFollowable = false
            )
        } else {
            val shopInfo = getPartnerInfo(partnerId, partnerType)
            mapPartnerInfoFromShop(shopInfo)
        }
    }

    private fun checkIsFollowedShop() {
        launchCatchError(block = {
            val channel = _observableChanelInfo.value?.copy()
            if (channel != null) _observablePartnerInfo.value = getPartnerInfo(channel)
        }, onError = {})
    }

    private suspend fun getPartnerInfo(partnerId: Long, partnerType: PartnerType) = withContext(dispatchers.io) {
            getPartnerInfoUseCase.params = GetPartnerInfoUseCase.createParam(partnerId.toInt(), partnerType)
            getPartnerInfoUseCase.executeOnBackground()
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
        }, onError = {
            _observableSocketInfo.value = PlaySocketInfo.RECONNECT
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
            quickReply = mapQuickReply(channel.quickReply),
            totalView = mapTotalViews(channel.totalViews)
    )

    private fun mapChannelInfo(channel: Channel) = ChannelInfoUiModel(
            id = channel.channelId,
            title = channel.title,
            description = channel.description,
            isLive = channel.videoStream.isLive,
            moderatorName = channel.moderatorName,
            partnerId = channel.partnerId,
            partnerType = channel.partnerType,
            contentId = channel.contentId,
            contentType = channel.contentType,
            likeType = channel.likeType
    )

    private fun mapPinnedMessage(partnerName: String, pinnedMessage: PinnedMessage) = PinnedMessageUiModel(
            applink = pinnedMessage.redirectUrl,
            partnerName = partnerName,
            title = pinnedMessage.title,
            shouldRemove = pinnedMessage.pinnedMessageId <= 0 || pinnedMessage.title.isEmpty()
    )

    private fun mapVideoStream(videoStream: VideoStream, isActive: Boolean) = VideoStreamUiModel(
            uriString = videoStream.config.streamUrl,
            channelType = if (videoStream.isLive
                    && videoStream.type.equals(PlayChannelType.Live.value, true))
                PlayChannelType.Live else PlayChannelType.VOD,
            isActive = isActive
    )

    private fun mapQuickReply(quickReplyList: List<String>) = QuickReplyUiModel(quickReplyList)
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
            isBanned = false,
            isFreeze = channel.isFreeze,
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
        playManager.muteVideo(false)
        playManager.setRepeatMode(false)
    }

    //region mock
    private fun initMockChat() {
        launch(dispatchers.io) {

            var index = 0
            while (isActive) {
                delay(3000)
                _observableNewChat.postValue(
                        mapPlayChat(
                                PlayChat(
                                        message = "test ${++index}",
                                        user = PlayChat.UserData(name = "YoMamen")
                                )
                        )
                )
            }
        }
    }

    private fun initMockFreeze() {
        launch(dispatchers.main) {
            delay(10000)
            _observableEvent.value = EventUiModel(isBanned = false, isFreeze = true, freezeTitle = "Freeze title", freezeMessage = "freeze message", freezeButtonTitle = "Freeze Button", freezeButtonUrl = "tokopedia://play/2")
        }
    }

    private fun initMockBanned() {
        launch(dispatchers.main) {
            delay(10000)
            _observableEvent.value = EventUiModel(isBanned = true, isFreeze = false, bannedTitle = "You are banned", bannedMessage = "You are banned for spamming", bannedButtonTitle = "Back to Home")
        }
    }
    //endregion

    companion object {
        private const val MAX_RETRY_CHANNEL_INFO = 3
    }
}
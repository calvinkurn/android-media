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
import com.tokopedia.play.util.toCompactAmountString
import com.tokopedia.play.view.type.KeyboardState
import com.tokopedia.play.view.type.PlayVideoType
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
        @ApplicationContext
        private val applicationContext: Context,
        private val playManager: TokopediaPlayManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val playSocket: PlaySocket,
        private val userSessionInterface: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    val observableVOD: LiveData<ExoPlayer>
        get() = _observableVOD
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
            _observableVideoProperty.value = VideoPropertyUiModel(it.videoType, _observableVideoProperty.value?.state
                    ?: TokopediaPlayVideoState.NotConfigured)
        }
        addSource(playManager.getObservablePlayVideoState()) {
            _observableVideoProperty.value = VideoPropertyUiModel(_observableVideoProperty.value?.type ?: PlayVideoType.Unknown, it)
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

    var isLive: Boolean = false
    var contentId: Int = 0
    var contentType: Int = 0
    var likeType: Int = 0

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

    fun showKeyboard(estimatedKeyboardHeight: Int) {
        _observableKeyboardState.value =
                if (_observableVideoStream.value?.videoType?.isLive == true) KeyboardState.Shown(estimatedKeyboardHeight)
                else KeyboardState.Hidden
    }

    fun hideKeyboard() {
        _observableKeyboardState.value = KeyboardState.Hidden
    }

    fun getChannelInfo(channelId: String) {
        launchCatchError(block = {
            val channel = withContext(dispatchers.io) {
                getChannelInfoUseCase.channelId = channelId
                return@withContext getChannelInfoUseCase.executeOnBackground()
            }
            /**
             * If Live => start web socket
             */
            getPartnerInfo(channel)
            // TODO("remove, for testing")
//            channel.videoStream = VideoStream(
//                    "vertical",
//                    "live",
//                    true,
//                    VideoStream.Config(streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"))

            setStateLiveOrVod(channel)
            setContentIdAndType(channel)
            if (channel.videoStream.isLive
                    && channel.videoStream.type.equals(PlayVideoType.Live.value, true))
                startWebSocket(channelId, channel.gcToken, channel.settings)
            playVideoStream(channel)

            val completeInfoUiModel = createCompleteInfoModel(channel)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableTotalViews.value = completeInfoUiModel.totalView
            _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
            _observableQuickReply.value = completeInfoUiModel.quickReply
            _observableVideoStream.value = completeInfoUiModel.videoStream
            _observableEvent.value = mapEvent(channel)

            val totalLike = getTotalLikes(channel.contentId, channel.contentType)
            _observableTotalLikes.value = mapTotalLikes(totalLike)

            if (userSessionInterface.isLoggedIn) {
                val isLiked = getIsLike(channel.contentId, channel.contentType)
                _observableIsLikeContent.value = isLiked
            }

        }) {
            if (it !is CancellationException) _observableGetChannelInfo.value = Fail(it)
        }
    }

    fun destroy() {
        playSocket.destroy()
    }

    fun sendChat(message: String) {
        if (!userSessionInterface.isLoggedIn)
            return

        val cleanMessage = message.trimMultipleNewlines()
        playSocket.send(cleanMessage, onSuccess = {
            _observableNewChat.value = mapPlayChat(
                    PlayChat(
                            message = cleanMessage,
                            user = PlayChat.UserData(
                                    id = userSessionInterface.userId,
                                    name = userSessionInterface.name,
                                    image = userSessionInterface.profilePicture)
                    )
            )
        })
    }

    fun changeLikeCount(shouldLike: Boolean) {
        val currentTotalLike = _observableTotalLikes.value ?: TotalLikeUiModel.empty()
        if (!hasWordsOrDotsRegex.containsMatchIn(currentTotalLike.totalLikeFormatted)) {
            val finalTotalLike = currentTotalLike.totalLike + (if (shouldLike) 1 else -1)
            _observableTotalLikes.value = TotalLikeUiModel(
                    finalTotalLike,
                    finalTotalLike.toCompactAmountString(amountStringStepArray)
            )
        }
    }

    private suspend fun getTotalLikes(contentId: Int, contentType: Int) = withContext(dispatchers.io) {
        getTotalLikeUseCase.params = GetTotalLikeUseCase.createParam(contentId, contentType, isLive)
        getTotalLikeUseCase.executeOnBackground()
    }

    private suspend fun getIsLike(contentId: Int, contentType: Int) = withContext(dispatchers.io) {
        getIsLikeUseCase.params = GetIsLikeUseCase.createParam(contentId, contentType)
        getIsLikeUseCase.executeOnBackground()
    }

    private fun getPartnerInfo(channel: Channel) {
        val partnerType = PartnerType.getTypeByValue(channel.partnerType)
        val partnerId = channel.partnerId
        if (partnerType == PartnerType.ADMIN) {
            _observablePartnerInfo.value = PartnerInfoUiModel(
                    id = partnerId,
                    name = channel.moderatorName,
                    type = partnerType,
                    isFollowed = true
            )
            return
        } else {
            getPartnerInfo(partnerId, partnerType)
        }
    }

    private fun getPartnerInfo(partnerId: Long, partnerType: PartnerType) = launchCatchError(block = {
        val response = withContext(dispatchers.io) {
            getPartnerInfoUseCase.params = GetPartnerInfoUseCase.createParam(partnerId.toInt(), partnerType)
            getPartnerInfoUseCase.executeOnBackground()
        }

        _observablePartnerInfo.value = mapPartnerInfoFromShop(response)
    }, onError = {

    })

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
                                            && result.userId.equals(userSessionInterface.userId, true))
                        }
                    }
                }
            }
        }, onReconnect = {
            _observableSocketInfo.value = PlaySocketInfo.RECONNECT
        }, onError = {
            _observableSocketInfo.value = PlaySocketInfo.ERROR
        })
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean) {
        playManager.safePlayVideoWithUriString(urlString, isLive)
    }

    private fun playVideoStream(channel: Channel) {
        if (channel.isActive) {
            startVideoWithUrlString(channel.videoStream.config.streamUrl, channel.videoStream.isLive)
        }
    }

    private fun setStateLiveOrVod(channel: Channel) {
        isLive = channel.videoStream.isLive
    }

    private fun setContentIdAndType(channel: Channel) {
        contentId = channel.contentId
        contentType = channel.contentType
        likeType = channel.likeType
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
            description = channel.description
    )

    private fun mapPinnedMessage(partnerName: String, pinnedMessage: PinnedMessage) = PinnedMessageUiModel(
            applink = pinnedMessage.redirectUrl,
            partnerName = partnerName,
            title = pinnedMessage.title,
            shouldRemove = pinnedMessage.pinnedMessageId <= 0 || pinnedMessage.title.isEmpty()
    )

    private fun mapVideoStream(videoStream: VideoStream, isActive: Boolean) = VideoStreamUiModel(
            uriString = videoStream.config.streamUrl,
            videoType = if (videoStream.isLive
                    && videoStream.type.equals(PlayVideoType.Live.value, true))
                PlayVideoType.Live else PlayVideoType.VOD,
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
            isSelfMessage = playChat.user.id == userSessionInterface.userId
    )

    private fun mapPartnerInfoFromShop(shopInfo: ShopInfo) = PartnerInfoUiModel(
            id = shopInfo.shopCore.shopId.toLong(),
            name = shopInfo.shopCore.name,
            type = PartnerType.SHOP,
            isFollowed = shopInfo.favoriteData.alreadyFavorited == 1
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
        hideKeyboard()
    }

    private fun stopPlayer() {
        playManager.stopPlayer()
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
}
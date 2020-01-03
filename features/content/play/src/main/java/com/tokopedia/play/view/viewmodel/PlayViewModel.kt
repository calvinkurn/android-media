package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.data.websocket.PlaySocketInfo
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.domain.GetShopInfoUseCase
import com.tokopedia.play.domain.GetVideoStreamUseCase
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.PlayVideoType
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playManager: TokopediaPlayManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getVideoStreamUseCase: GetVideoStreamUseCase,
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val playSocket: PlaySocket,
        private val userSessionInterface: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    val observableVOD: LiveData<ExoPlayer>
        get() = _observableVOD
    private val _observableVOD = MutableLiveData<ExoPlayer>()

    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    val observableGetChannelInfo: LiveData<Result<ChannelInfoUiModel>> = _observableGetChannelInfo

    private val _observableVideoStream = MutableLiveData<VideoStreamUiModel>()
    val observableVideoStream: LiveData<VideoStreamUiModel> = _observableVideoStream

    private val _observableSocketInfo = MutableLiveData<PlaySocketInfo>()
    val observableSocketInfo: LiveData<PlaySocketInfo> = _observableSocketInfo

    private val _observableChatList = MutableLiveData<PlayChat>()
    val observableChatList: LiveData<PlayChat> = _observableChatList

    private val _observableTotalViews = MutableLiveData<TotalViewUiModel>()
    val observableTotalViews: LiveData<TotalViewUiModel> = _observableTotalViews

    private val _observablePartnerInfo: MutableLiveData<PartnerInfoUiModel> = MutableLiveData()
    val observablePartnerInfo: LiveData<PartnerInfoUiModel> = _observablePartnerInfo

    private val _observableQuickReply = MutableLiveData<QuickReplyUiModel>()
    val observableQuickReply: LiveData<QuickReplyUiModel> = _observableQuickReply

    private val _observableEvent = MutableLiveData<EventUiModel>()
    val observableEvent: LiveData<EventUiModel> = _observableEvent

    private val _observablePinnedMessage = MediatorLiveData<PinnedMessageUiModel>().apply {
        addSource(observablePartnerInfo) {
            val currentValue = value
            if (currentValue != null) value = currentValue.copy(
                    partnerName = it.name
            )
        }
    }
    val observablePinnedMessage: LiveData<PinnedMessageUiModel> = _observablePinnedMessage

    val observableVideoProperty: LiveData<VideoPropertyUiModel> = MediatorLiveData<VideoPropertyUiModel>().apply {
        addSource(observableVideoStream) {
            value = VideoPropertyUiModel(it.videoType, value?.state
                    ?: TokopediaPlayVideoState.NotConfigured)
        }
        addSource(playManager.getObservablePlayVideoState()) {
            value = VideoPropertyUiModel(value?.type ?: PlayVideoType.Unknown, it)
        }
    }

    var isLive: Boolean = false

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
    }

    fun getDurationCurrentVideo(): Long {
        return playManager.getDurationVideo()
    }

    fun getChannelInfo(channelId: String) {
        launchCatchError(block = {
            val (channel, videoStream) = withContext(dispatchers.io) {
                getChannelInfoUseCase.channelId = channelId
                val channel = getChannelInfoUseCase.executeOnBackground()

                getVideoStreamUseCase.channelId = channelId
                val videoStream = getVideoStreamUseCase.executeOnBackground()

                return@withContext Pair(channel, videoStream)
            }
            /**
             * If Live => start web socket
             */
            getPartnerInfo(channel)
            setStateLiveOrVod(channel)
            if (videoStream.isLive) startWebSocket(channelId, channel.gcToken, channel.settings)
            playVideoStream(videoStream)

            val completeInfoUiModel = createCompleteInfoModel(channel, videoStream)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableTotalViews.value = completeInfoUiModel.totalView
            _observablePinnedMessage.value = completeInfoUiModel.pinnedMessage
            _observableQuickReply.value = completeInfoUiModel.quickReply
            _observableVideoStream.value = completeInfoUiModel.videoStream
            _observableEvent.value = mapEvent(channel)
        }) {
            //TODO("Change it later")
            _observableGetChannelInfo.value = Fail(it)
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
            _observableChatList.value = PlayChat("", "", cleanMessage,
                    PlayChat.UserData(userSessionInterface.userId, userSessionInterface.name, userSessionInterface.profilePicture))
        })
    }

    private fun getPartnerInfo(channel: Channel) {
        val partnerType = PartnerType.getTypeByValue(channel.partnerType)
        val partnerId = channel.partnerId
        if (partnerType == PartnerType.SHOP) getShopPartnerInfo(partnerId)

        if (partnerType == PartnerType.ADMIN) {
            _observablePartnerInfo.value = PartnerInfoUiModel(
                    id = partnerId,
                    name = channel.moderatorName,
                    type = partnerType,
                    isFollowed = true
            )
            return
        }

        if (partnerType == PartnerType.INFLUENCER) {
            _observablePartnerInfo.value = PartnerInfoUiModel(
                    id = partnerId,
                    name = "", //TODO("Get From Kol Api")
                    type = partnerType,
                    isFollowed = false //TODO("Get From Kol Api")
            )
            return
        }
    }

    private fun getShopPartnerInfo(shopId: Long) = launchCatchError(block = {
        val response = withContext(dispatchers.io) {
            getShopInfoUseCase.params = GetShopInfoUseCase.createParam(shopId.toString())
            getShopInfoUseCase.executeOnBackground()
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

                    }
                    is TotalView -> {
                        _observableTotalViews.value = mapTotalViews(result)
                    }
                    is PlayChat -> {
                        _observableChatList.value = result
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
                    is VideoStream -> {
                        val videoStreamUiModel = mapVideoStream(result)
                        startVideoWithUrlString(videoStreamUiModel.uriString, videoStreamUiModel.videoType.isLive)
                        _observableVideoStream.value = videoStreamUiModel
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
        if (_observableVOD.value == null) _observableVOD.value = playManager.videoPlayer
    }

    private fun playVideoStream(videoStream: VideoStream) {
        if (videoStream.isActive) {
            startVideoWithUrlString(videoStream.androidStreamHd, videoStream.isLive)
        }
    }

    private fun setStateLiveOrVod(channel: Channel) {
        isLive = channel.endTime < System.currentTimeMillis()
    }

    private fun createCompleteInfoModel(channel: Channel, videoStream: VideoStream) = PlayCompleteInfoUiModel(
            channelInfo = mapChannelInfo(channel),
            videoStream = mapVideoStream(videoStream),
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

    private fun mapVideoStream(videoStream: VideoStream) = VideoStreamUiModel(
            uriString = videoStream.androidStreamHd,
            videoType = if (videoStream.isLive) PlayVideoType.Live else PlayVideoType.VOD,
            isActive = videoStream.isActive
    )

    private fun mapQuickReply(quickReplyList: List<String>) = QuickReplyUiModel(quickReplyList)
    private fun mapQuickReply(quickReply: QuickReply) = mapQuickReply(quickReply.data)

    private fun mapTotalViews(totalViewString: String) = TotalViewUiModel(totalViewString)
    private fun mapTotalViews(totalView: TotalView) = mapTotalViews(totalView.totalView)

    private fun mapPartnerInfoFromShop(shopInfo: ShopInfo) = PartnerInfoUiModel(
            id = shopInfo.shopCore.shopId.toLong(),
            name = shopInfo.shopCore.name,
            type = PartnerType.SHOP,
            isFollowed = shopInfo.favoriteData.alreadyFavorited == 1
    )

    private fun mapEvent(channel: Channel) = EventUiModel(
                isBanned = false,
                isFreeze = false,
                bannedMessage = channel.bannedMsg,
                bannedTitle = channel.bannedTitle,
                bannedButtonTitle = channel.bannedButtonTitle,
                bannedButtonUrl = channel.bannedButtonUrl,
                freezeMessage = channel.freezeChannelState.desc,
                freezeTitle = channel.freezeChannelState.title,
                freezeButtonTitle = channel.freezeChannelState.btnTitle,
                freezeButtonUrl = channel.freezeChannelState.btnAppLink
        )

    private fun String.trimMultipleNewlines() = trim().replace(Regex("(\\n+)"), "\n")
}
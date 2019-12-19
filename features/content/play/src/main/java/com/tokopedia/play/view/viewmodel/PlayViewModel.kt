package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PARTNER_NAME_ADMIN
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
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
import timber.log.Timber
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

    private val _observableChatListSocket = MutableLiveData<PlayChat>()
    val observableChatListSocket: LiveData<PlayChat> = _observableChatListSocket

    private val _observableTotalViewsSocket = MutableLiveData<TotalViewUiModel>()
    val observableTotalViewsSocket: LiveData<TotalViewUiModel> = _observableTotalViewsSocket

    private val _observablePartnerInfo: MutableLiveData<PartnerInfoUiModel> = MutableLiveData()
    val observablePartnerInfo: LiveData<PartnerInfoUiModel> = _observablePartnerInfo

    private val _observableQuickReplySocket = MutableLiveData<QuickReplyUiModel>()
    val observableQuickReplySocket: LiveData<QuickReplyUiModel> = _observableQuickReplySocket

    private val _observableBannedFreezeSocket = MutableLiveData<BannedFreeze>()
    val observableBannedFreezeSocket: LiveData<BannedFreeze> = _observableBannedFreezeSocket

    private val _observablePinnedMessageSocket = MediatorLiveData<PinnedMessageUiModel>().apply {
        addSource(observablePartnerInfo) {
            val currentValue = value
            if (currentValue != null) value = currentValue.copy(
                    partnerName = it.name
            )
        }
    }
    val observablePinnedMessageSocket: LiveData<PinnedMessageUiModel> = _observablePinnedMessageSocket

    val observableVideoProperty: LiveData<VideoPropertyUiModel> = MediatorLiveData<VideoPropertyUiModel>().apply {
        addSource(observableVideoStream) {
            value = VideoPropertyUiModel(it.videoType, value?.state
                    ?: TokopediaPlayVideoState.NotConfigured)
        }
        addSource(playManager.getObservablePlayVideoState()) {
            value = VideoPropertyUiModel(value?.type ?: PlayVideoType.Unknown, it)
        }
    }

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
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
            getPartnerInfo(PartnerType.getTypeByValue(channel.partnerType), channel.partnerId)
            if (videoStream.isLive) startWebSocket(channelId, channel.gcToken, channel.settings)
            playVideoStream(videoStream)

            val completeInfoUiModel = createCompleteInfoModel(channel, videoStream)

            _observableGetChannelInfo.value = Success(completeInfoUiModel.channelInfo)
            _observableTotalViewsSocket.value = completeInfoUiModel.totalView
            _observablePinnedMessageSocket.value = completeInfoUiModel.pinnedMessage
            _observableQuickReplySocket.value = completeInfoUiModel.quickReply
            _observableVideoStream.value = completeInfoUiModel.videoStream
        }) {
            //TODO("Change it later")
            _observableGetChannelInfo.value = Fail(it)
        }
    }

    // TODO don't forget to destroy socket
    fun destroy() {
        playSocket.destroy()
    }

    fun sendChat(message: String) {
        if (userSessionInterface.isLoggedIn) {
            playSocket.send(message, onSuccess = {
                _observableChatListSocket.value = PlayChat("", "", message,
                        PlayChat.UserData(userSessionInterface.userId, userSessionInterface.name, userSessionInterface.profilePicture))
            })
        } else {
            // TODO route to login activity
        }

    }

    private fun getPartnerInfo(partnerType: PartnerType, partnerId: Long) {
        if (partnerType == PartnerType.SHOP) getShopPartnerInfo(partnerId)

        if (partnerType == PartnerType.ADMIN) {
            _observablePartnerInfo.value = PartnerInfoUiModel(
                    id = partnerId,
                    name = PARTNER_NAME_ADMIN,
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
        playSocket.connect(onOpen = {
            Timber.tag("PlaySocket").d("onOpen")
            // Todo, handle on open web socket
        }, onClose = {
            Timber.tag("PlaySocket").d("onClose")
            // Todo, handle on close web socket
        }, onMessageReceived = { response ->
            launch {
                val result = withContext(dispatchers.io) {
                    val socketMapper = PlaySocketMapper(response)
                    socketMapper.mapping()
                }
                when (result) {
                    is TotalLike -> {

                    }
                    is TotalView -> {
                        _observableTotalViewsSocket.value = mapTotalViews(result)
                    }
                    is PlayChat -> {
                        _observableChatListSocket.value = result
                    }
                    is PinnedMessage -> {
                        val partnerName = _observablePartnerInfo.value?.name.orEmpty()
                        _observablePinnedMessageSocket.value = mapPinnedMessage(partnerName, result)
                    }
                    is QuickReply -> {
                        _observableQuickReplySocket.value = mapQuickReply(result)
                    }
                    is BannedFreeze -> {
                        _observableBannedFreezeSocket.value = result
                    }
                    is VideoStream -> {
                        val videoStreamUiModel = mapVideoStream(result)
                        startVideoWithUrlString(videoStreamUiModel.uriString, videoStreamUiModel.videoType.isLive)
                        _observableVideoStream.value = videoStreamUiModel
                    }
                }
            }
        }, onError = {
            Timber.tag("PlaySocket").e(it)
            // Todo, handle on error web socket
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
}
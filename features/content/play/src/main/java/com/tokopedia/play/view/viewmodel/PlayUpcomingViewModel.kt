package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.SocketCredential
import com.tokopedia.play.data.UpcomingChannelUpdateActive
import com.tokopedia.play.data.UpcomingChannelUpdateLive
import com.tokopedia.play.data.ssemapper.PlaySSEMapper
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.GetSocketCredentialUseCase
import com.tokopedia.play.domain.PlayChannelReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.setValue
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.share.PlayShareExperienceData
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.state.*
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.PlayChannelSSEPageSource
import com.tokopedia.play_common.sse.model.SSEAction
import com.tokopedia.play_common.sse.model.SSECloseReason
import com.tokopedia.play_common.sse.model.SSEResponse
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
class PlayUpcomingViewModel @Inject constructor(
    private val getChannelStatusUseCase: GetChannelStatusUseCase,
    private val playChannelReminderUseCase: PlayChannelReminderUseCase,
    private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val playUiModelMapper: PlayUiModelMapper,
    private val playAnalytic: PlayNewAnalytic,
    private val playChannelSSE: PlayChannelSSE,
    private val repo: PlayViewerRepository,
    private val playShareExperience: PlayShareExperience,
): ViewModel() {

    private var mChannelId: String = ""

    private var mChannelData: PlayChannelData? = null

    val channelType: PlayChannelType
        get() = _channelDetail.value.channelInfo.channelType

    val isReminderSet: Boolean
        get() = _upcomingInfo.value.isReminderSet

    val partnerId: Long?
        get() = mChannelData?.partnerInfo?.id

    private val _uiEvent = MutableSharedFlow<PlayUpcomingUiEvent>(extraBufferCapacity = 50)

    private val _channelDetail = MutableStateFlow(PlayChannelDetailUiModel())
    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _upcomingInfo = MutableStateFlow(PlayUpcomingUiModel())
    private val _upcomingState = MutableStateFlow<PlayUpcomingState>(PlayUpcomingState.Unknown)

    private val _partnerUiState = _partnerInfo.map {
        PlayUpcomingPartnerUiState(it.name, it.status)
    }.flowOn(dispatchers.computation)

    private val _upcomingInfoUiState = combine(
        _upcomingInfo, _upcomingState
    ) { info, state ->
        PlayUpcomingInfoUiState(
            generalInfo = PlayUpcomingGeneralInfo(
                title = info.title,
                coverUrl = info.coverUrl,
                startTime = info.startTime,
                waitingDuration = info.refreshWaitingDuration,
            ),
            state = state
        )
    }.flowOn(dispatchers.computation)

    private val _shareUiState = _channelDetail.map {
        PlayUpcomingShareUiState(shouldShow = it.shareInfo.shouldShow)
    }.flowOn(dispatchers.computation)


    val uiState: Flow<PlayUpcomingUiState> = combine(
        _partnerUiState.distinctUntilChanged(),
        _upcomingInfoUiState.distinctUntilChanged(),
        _shareUiState.distinctUntilChanged(),
    ) { partner, upcomingInfo, share ->
        PlayUpcomingUiState(
            partner = partner,
            upcomingInfo = upcomingInfo,
            share = share
        )
    }.flowOn(dispatchers.computation)

    val uiEvent: Flow<PlayUpcomingUiEvent>
        get() = _uiEvent

    private var sseJob: Job? = null

    val latestChannelData: PlayChannelData
        get() {
            val channelData = mChannelData ?: error("Channel Data should not be null")

            return channelData.copy(
                upcomingInfo = channelData.upcomingInfo.copy(
                    isReminderSet = _upcomingInfo.value.isReminderSet,
                    isAlreadyLive = _upcomingState.value == PlayUpcomingState.WatchNow
                )
            )
        }

    fun initPage(channelId: String, channelData: PlayChannelData) {
        this.mChannelId = channelId
        this.mChannelData = channelData

        _partnerInfo.value = channelData.partnerInfo
        _channelDetail.value = channelData.channelDetail
        _upcomingInfo.value = channelData.upcomingInfo

        updateUpcomingState(channelData.upcomingInfo)
        updateStatusInfo(mChannelId, false)
        updatePartnerInfo(channelData.partnerInfo)
    }

    override fun onCleared() {
        super.onCleared()
        stopSSE()
    }

    private fun updateUpcomingState(upcomingInfo: PlayUpcomingUiModel) {
        viewModelScope.launch {
            if(_upcomingState.value == PlayUpcomingState.Unknown) {
                _upcomingState.emit(
                    when {
                        upcomingInfo.isAlreadyLive -> PlayUpcomingState.WatchNow
                        upcomingInfo.isReminderSet -> PlayUpcomingState.Reminded
                        else -> PlayUpcomingState.RemindMe
                    }
                )
            }
        }
    }

    private fun updateStatusInfo(channelId: String, withAction: Boolean = true) {
        viewModelScope.launchCatchError(block = {
            val channelStatus = getChannelStatus(channelId)

            if(withAction) {
                if(playUiModelMapper.mapStatus(channelStatus).isActive) {
                    _upcomingState.emit(PlayUpcomingState.WatchNow)
                }
                else {
                    performRefreshWaitingDuration()
                }
            }

            _upcomingInfo.setValue { copy(refreshWaitingDuration = playUiModelMapper.mapWaitingDuration(channelStatus)) }
        }, onError = {
            if(withAction) _upcomingState.emit(PlayUpcomingState.Refresh)

            _upcomingInfo.setValue { copy(refreshWaitingDuration = PlayUpcomingUiModel.REFRESH_WAITING_DURATION) }

            _uiEvent.emit(PlayUpcomingUiEvent.ShowInfoWithActionEvent(UiString.Resource(R.string.play_upcoming_channel_not_started)){})
        })
    }

    private fun updatePartnerInfo(partnerInfo: PlayPartnerInfo) {
        if (partnerInfo.status !is PlayPartnerFollowStatus.NotFollowable) {
            viewModelScope.launchCatchError(block = {
                val isFollowing = getFollowingStatus(partnerInfo)
                val result = if(isFollowing) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(result)) }
            }, onError = {

            })
        }
    }

    private suspend fun getFollowingStatus(partnerInfo: PlayPartnerInfo) : Boolean {
        return if (userSession.isLoggedIn) {
            when(partnerInfo.type){
                PartnerType.Shop -> repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                else -> repo.getFollowingKOL(partnerInfo.id.toString())
            }
        } else false
    }

    private suspend fun getChannelStatus(channelId: String) = withContext(dispatchers.io) {
        getChannelStatusUseCase.apply {
            setRequestParams(GetChannelStatusUseCase.createParams(arrayOf(channelId)))
        }.executeOnBackground()
    }

    fun submitAction(action: PlayUpcomingAction) {
        when(action) {
            ImpressUpcomingChannel -> handleImpressUpcomingChannel()
            ClickUpcomingButton -> handleClickUpcomingButton()
            UpcomingTimerFinish -> handleUpcomingTimerFinish()
            ClickFollowUpcomingAction -> handleClickFollow(isFromLogin = false)
            ClickPartnerNameUpcomingAction -> handleClickPartnerName()
            is OpenUpcomingPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
            CopyLinkUpcomingAction -> handleCopyLink()
            ClickShareUpcomingAction -> handleClickShareIcon()
            ShowShareExperienceUpcomingAction -> handleOpenSharingOption(false)
            ScreenshotTakenUpcomingAction -> handleOpenSharingOption(true)
            CloseSharingOptionUpcomingAction -> handleCloseSharingOption()
            is ClickSharingOptionUpcomingAction -> handleSharingOption(action.shareModel)
            is SharePermissionUpcomingAction -> handleSharePermission(action.label)
        }
    }

    private fun handleImpressUpcomingChannel() {
        playAnalytic.impressUpcomingPage(mChannelId)
    }

    private fun handleClickUpcomingButton() {
        val currState = _upcomingState.value
        _upcomingState.value = PlayUpcomingState.Loading

        when(currState) {
            PlayUpcomingState.WatchNow -> handleWatchNowUpcomingChannel()
            PlayUpcomingState.RemindMe -> handleRemindMeUpcomingChannel(userClick = true)
            PlayUpcomingState.Refresh -> handleRefreshUpcomingChannel()
            else -> {}
        }
    }

    private fun handleRemindMeUpcomingChannel(userClick: Boolean)  {

        suspend fun failedRemindMe() {
            _upcomingState.emit(PlayUpcomingState.RemindMe)
            _uiEvent.emit(PlayUpcomingUiEvent.RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
        }

        if(userClick) playAnalytic.clickRemindMe(mChannelId)

        needLogin(REQUEST_CODE_LOGIN_REMIND_ME) {
            viewModelScope.launchCatchError(block = {

                mChannelData?.let {
                    val status: Boolean

                    withContext(dispatchers.io) {
                        playChannelReminderUseCase.setRequestParams(PlayChannelReminderUseCase.createParams(it.id, true))
                        val response = playChannelReminderUseCase.executeOnBackground()
                        status = PlayChannelReminderUseCase.checkRequestSuccess(response)
                    }

                    if(!status) failedRemindMe()
                    else {
                        _upcomingState.emit(PlayUpcomingState.Reminded)
                        _upcomingInfo.setValue { copy(isReminderSet = status) }

                        _uiEvent.emit(PlayUpcomingUiEvent.RemindMeEvent(message = UiString.Resource(R.string.play_remind_me_success), isSuccess = status))
                    }
                } ?: failedRemindMe()
            }) {
                failedRemindMe()
            }
        }


    }

    private fun handleWatchNowUpcomingChannel() {
        playAnalytic.clickWatchNow(mChannelId)
        stopSSE()

        viewModelScope.launch {
            _uiEvent.emit(PlayUpcomingUiEvent.RefreshChannelEvent)
        }
    }

    private fun handleRefreshUpcomingChannel() {
        updateStatusInfo(mChannelId)
    }

    private fun handleUpcomingTimerFinish() {
        performRefreshWaitingDuration()
    }

    private fun performRefreshWaitingDuration() {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
            _upcomingState.emit(PlayUpcomingState.WaitingRefreshDuration)
            _uiEvent.emit(
                PlayUpcomingUiEvent.ShowInfoEvent(
                    UiString.Resource(R.string.play_upcoming_channel_not_started)
                )
            )

            delay(_upcomingInfo.value.refreshWaitingDuration.toLong())

            val isAlreadyLive = _upcomingState.value == PlayUpcomingState.WatchNow
            if(!isAlreadyLive) {
                _upcomingState.emit(PlayUpcomingState.Refresh)
            }
        }) {

        }
    }

    private fun handleClickFollow(isFromLogin: Boolean) = needLogin(REQUEST_CODE_LOGIN_FOLLOW) {
        val action = doFollowUnfollow(shouldForceFollow = isFromLogin) ?: return@needLogin
        val shopId = _partnerInfo.value.id
        if(_partnerInfo.value.type == PartnerType.Shop) playAnalytic.clickFollowShop(mChannelId, channelType, shopId.toString(), action.value)
    }

    private fun handleClickPartnerName() {
        viewModelScope.launch {
            val partnerInfo = _partnerInfo.value

            when (partnerInfo.type) {
                PartnerType.Shop -> {
                    playAnalytic.clickShop(mChannelId, channelType, partnerInfo.id.toString())
                    _uiEvent.emit(PlayUpcomingUiEvent.OpenPageEvent(ApplinkConst.SHOP, listOf(partnerInfo.id.toString())))
                }
                PartnerType.Buyer -> _uiEvent.emit(PlayUpcomingUiEvent.OpenPageEvent(ApplinkConst.PROFILE, listOf(partnerInfo.id.toString())))
                else -> {}
            }
        }
    }

    private fun doFollowUnfollow(shouldForceFollow: Boolean): PartnerFollowAction? {
        val channelData = mChannelData ?: return null
        val shopId = channelData.partnerInfo.id

        val followStatus = _partnerInfo.value.status as? PlayPartnerFollowStatus.Followable ?: return null
        val shouldFollow = if (shouldForceFollow) true else followStatus.followStatus == PartnerFollowableStatus.NotFollowed
        val followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow

        val result = if(shouldFollow) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed

        _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(result)) }

        viewModelScope.launchCatchError(block = {
            if(channelData.partnerInfo.type == PartnerType.Shop){
                repo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction,
                )
            } else {
                repo.postFollowKol(shopId.toString())
            }
        }) {}

        return followAction
    }

    private fun copyLink() {
        val shareInfo = _channelDetail.value.shareInfo

        viewModelScope.launch {
            _uiEvent.emit(
                PlayUpcomingUiEvent.CopyToClipboardEvent(shareInfo.content)
            )

            _uiEvent.emit(
                PlayUpcomingUiEvent.ShowInfoEvent(
                    UiString.Resource(R.string.play_link_copied)
                )
            )
        }
    }

    private fun handleCopyLink() {
        viewModelScope.launch { copyLink() }
    }

    private fun handleClickShareIcon() {
        viewModelScope.launch {
            playAnalytic.clickShareButton(mChannelId, partnerId, channelType.value)

            _uiEvent.emit(
                PlayUpcomingUiEvent.SaveTemporarySharingImage(imageUrl = _channelDetail.value.channelInfo.coverUrl)
            )
        }
    }

    private fun handleOpenSharingOption(isScreenshot: Boolean) {
        viewModelScope.launch {
            if(isScreenshot)
                playAnalytic.takeScreenshotForSharing(mChannelId, partnerId, channelType.value)

            if(playShareExperience.isCustomSharingAllow()) {
                playAnalytic.impressShareBottomSheet(mChannelId, partnerId, channelType.value)

                _uiEvent.emit(PlayUpcomingUiEvent.OpenSharingOptionEvent(
                    title = _channelDetail.value.channelInfo.title,
                    coverUrl = _channelDetail.value.channelInfo.coverUrl,
                    userId = userSession.userId,
                    channelId = mChannelId
                ))
            }
            else if(!isScreenshot) {
                copyLink()
            }
        }
    }

    private fun handleCloseSharingOption() {
        playAnalytic.closeShareBottomSheet(mChannelId, partnerId, channelType.value, playShareExperience.isScreenshotBottomSheet())
    }

    private fun handleSharingOption(shareModel: ShareModel) {
        viewModelScope.launch {
            playAnalytic.clickSharingOption(mChannelId, partnerId, channelType.value, shareModel.socialMediaName, playShareExperience.isScreenshotBottomSheet())

            val playShareExperienceData = getPlayShareExperienceData()

            playShareExperience
                .setShareModel(shareModel)
                .setData(playShareExperienceData)
                .createUrl(object: PlayShareExperience.Listener {
                    override fun onUrlCreated(
                        linkerShareData: LinkerShareResult?,
                        shareModel: ShareModel,
                        shareString: String
                    ) {
                        viewModelScope.launch {
                            _uiEvent.emit(PlayUpcomingUiEvent.CloseShareExperienceBottomSheet)
                            _uiEvent.emit(
                                PlayUpcomingUiEvent.OpenSelectedSharingOptionEvent(
                                    linkerShareData,
                                    shareModel,
                                    shareString
                                )
                            )
                        }
                    }

                    override fun onError(e: Exception) {
                        viewModelScope.launch {
                            _uiEvent.emit(PlayUpcomingUiEvent.CloseShareExperienceBottomSheet)
                            _uiEvent.emit(PlayUpcomingUiEvent.ErrorGenerateShareLink)
                        }
                    }
                }
            )
        }
    }

    private fun handleSharePermission(label: String) {
        playAnalytic.clickSharePermission(mChannelId, partnerId, channelType.value, label)
    }

    /**
     * SSE
     */
    fun startSSE(channelId: String) {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            val socketCredential = getSocketCredential()
            connectSSE(channelId, PlayChannelSSEPageSource.PlayUpcomingChannel.source, socketCredential.gcToken)
            playChannelSSE.listen().collect {
                when (it) {
                    is SSEAction.Message -> handleSSEMessage(it.message, channelId)
                    is SSEAction.Close -> {
                        if (it.reason == SSECloseReason.ERROR)
                            connectSSE(channelId, PlayChannelSSEPageSource.PlayUpcomingChannel.source, socketCredential.gcToken)
                    }
                }
            }
        }
    }

    private fun connectSSE(channelId: String, pageSource: String, gcToken: String) {
        playChannelSSE.connect(channelId, pageSource, gcToken)
    }

    private fun stopSSE() {
        sseJob?.cancel()
        playChannelSSE.close()
    }

    private suspend fun handleSSEMessage(message: SSEResponse, channelId: String) {
        val result = withContext(dispatchers.computation) {
            val sseMapper = PlaySSEMapper(message)
            sseMapper.mapping()
        }

        when(result) {
            is UpcomingChannelUpdateLive -> handleUpdateChannelStatus(result.channelId, channelId)
            is UpcomingChannelUpdateActive -> handleUpdateChannelStatus(result.channelId, channelId)
        }
    }

    private suspend fun handleUpdateChannelStatus(changedChannelId: String, currentChannelId: String) {
        if(changedChannelId == currentChannelId) {
            _upcomingState.emit(PlayUpcomingState.WatchNow)
            stopSSE()
        }
    }

    private suspend fun getSocketCredential(): SocketCredential = try {
        withContext(dispatchers.io) {
            return@withContext getSocketCredentialUseCase.executeOnBackground()
        }
    } catch (e: Throwable) {
        SocketCredential()
    }

    /**
     * Utility Function
     */
    private fun needLogin(requestCode: Int? = null, fn: () -> Unit) {
        if (userSession.isLoggedIn) fn()
        else {
            viewModelScope.launch {
                _uiEvent.emit(
                    PlayUpcomingUiEvent.OpenPageEvent(
                        applink = ApplinkConst.LOGIN,
                        requestCode = requestCode
                    )
                )
            }
        }
    }

    private fun handleOpenPageResult(isSuccess: Boolean, requestCode: Int) {
        if (!isSuccess) {
            if(requestCode == REQUEST_CODE_LOGIN_REMIND_ME) {
                viewModelScope.launch {
                    _upcomingState.value = PlayUpcomingState.RemindMe
                }
            }
            return
        }

        when (requestCode) {
            REQUEST_CODE_LOGIN_REMIND_ME -> handleRemindMeUpcomingChannel(userClick = false)
            REQUEST_CODE_LOGIN_FOLLOW -> handleClickFollow(isFromLogin = true)
            else -> {}
        }
    }

    private fun getPlayShareExperienceData(): PlayShareExperienceData {
        val (channelInfo, shareInfo) = _channelDetail.let {
            return@let Pair(it.value.channelInfo, it.value.shareInfo)
        }

        return PlayShareExperienceData(
            id = mChannelId,
            title = channelInfo.title,
            partnerName = _partnerInfo.value.name,
            coverUrl = channelInfo.coverUrl,
            redirectUrl = shareInfo.redirectUrl,
            textDescription = shareInfo.textDescription,
            metaTitle = shareInfo.metaTitle,
            metaDescription = shareInfo.metaDescription,
        )
    }

    companion object {
        const val REQUEST_CODE_LOGIN_REMIND_ME = 678
        const val REQUEST_CODE_LOGIN_FOLLOW = 679
    }
}
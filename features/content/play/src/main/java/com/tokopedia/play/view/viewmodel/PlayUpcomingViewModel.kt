package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.play.R
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
    private val _widgetState = MutableStateFlow(DescriptionUiState())

    private val _observableKolId = MutableLiveData<String>()

    private val _upcomingInfoUiState = combine(
        _upcomingInfo, _upcomingState
    ) { info, state ->
        PlayUpcomingInfoUiState(
            info = info,
            state = state
        )
    }.flowOn(dispatchers.computation)

    val uiState: Flow<PlayUpcomingUiState> = combine(
        _partnerInfo,
        _upcomingInfoUiState.distinctUntilChanged(),
        _channelDetail, _widgetState,
    ) { partner, upcomingInfo, channelDetail, widgetState ->
        PlayUpcomingUiState(
            partner = partner,
            upcomingInfo = upcomingInfo,
            channel = channelDetail,
            description = widgetState,
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

    val isExpanded: Boolean
            get() = _widgetState.value.isExpand

    val isWidgetShown: Boolean
        get() = _widgetState.value.isShown

    val remindState: PlayUpcomingState
        get() = _upcomingState.value

    val isCustomSharingAllowed: Boolean
        get() = playShareExperience.isCustomSharingAllow()

    val isSharingBottomSheet: Boolean
        get() = playShareExperience.isScreenshotBottomSheet()

    fun initPage(channelId: String, channelData: PlayChannelData) {
        this.mChannelId = channelId
        this.mChannelData = channelData

        _partnerInfo.value = channelData.partnerInfo
        _channelDetail.value = channelData.channelDetail
        _upcomingInfo.value = channelData.upcomingInfo

        updateUpcomingState(channelData.upcomingInfo)
        updateStatusInfo(mChannelId, false)
        updatePartnerInfo(channelData.partnerInfo)
        handleWidgetState(channelData.upcomingInfo.description.isNotBlank())
    }

    private fun handleWidgetState(isShown: Boolean){
        _widgetState.update { it.copy(isShown = isShown) }
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
                        upcomingInfo.isReminderSet -> PlayUpcomingState.ReminderStatus(isReminded = upcomingInfo.isReminderSet)
                        else -> PlayUpcomingState.ReminderStatus(isReminded = false)
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
        val isNeedToBeShown = if(userSession.isLoggedIn) partnerInfo.id.toString() != userSession.shopId && partnerInfo.id.toString() != userSession.userId else true
        if (partnerInfo.status !is PlayPartnerFollowStatus.NotFollowable && isNeedToBeShown) {
            viewModelScope.launchCatchError(block = {
                val isFollowing = getFollowingStatus(partnerInfo)
                val result = if(isFollowing) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(result)) }
            }, onError = {})
        } else  {
            _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.NotFollowable) }
        }
    }

    private suspend fun getFollowingStatus(partnerInfo: PlayPartnerInfo) : Boolean {
        return if (userSession.isLoggedIn) {
            when(partnerInfo.type){
                PartnerType.Shop -> repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                PartnerType.Buyer -> {
                    val data = repo.getFollowingKOL(partnerInfo.id.toString())
                    _observableKolId.value = data.second
                    data.first
                }
                else -> false
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
            ClickUpcomingButton -> handleClickUpcomingButton()
            UpcomingTimerFinish -> handleUpcomingTimerFinish()
            ClickFollowUpcomingAction -> handleClickFollow(isFromLogin = false)
            is ClickPartnerNameUpcomingAction -> handleClickPartnerName(action.appLink)
            is OpenUpcomingPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
            CopyLinkUpcomingAction -> handleCopyLink()
            ClickShareUpcomingAction -> handleClickShareIcon()
            ShowShareExperienceUpcomingAction -> handleOpenSharingOption(false)
            ScreenshotTakenUpcomingAction -> handleOpenSharingOption(true)
            is ClickSharingOptionUpcomingAction -> handleSharingOption(action.shareModel)
            ExpandDescriptionUpcomingAction -> handleExpandText()
            TapCover -> handleTapCover()
        }
    }

    private fun handleExpandText(){
        _widgetState.update { it.copy(isExpand = !it.isExpand) }
    }

    private fun handleTapCover(){
        if (_upcomingInfo.value.description.isNotBlank() && isExpanded) handleExpandText()
        else _widgetState.update { it.copy(isShown = !it.isShown) }
    }

    private fun handleClickUpcomingButton() {
        val currState = _upcomingState.value
        _upcomingState.value = PlayUpcomingState.Loading

        when(currState) {
            PlayUpcomingState.WatchNow -> handleWatchNowUpcomingChannel()
            is PlayUpcomingState.ReminderStatus -> handleRemindMeUpcomingChannel()
            PlayUpcomingState.Refresh -> handleRefreshUpcomingChannel()
            else -> {}
        }
    }

    private fun handleRemindMeUpcomingChannel() {
        suspend fun failedRemindMe() {
            _upcomingState.emit(PlayUpcomingState.ReminderStatus(isReminded = isReminderSet))
            _uiEvent.emit(PlayUpcomingUiEvent.RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
        }

        needLogin(REQUEST_CODE_LOGIN_REMIND_ME) {
            viewModelScope.launchCatchError(block = {

                mChannelData?.let {
                    val status: Boolean

                    withContext(dispatchers.io) {
                        playChannelReminderUseCase.setRequestParams(PlayChannelReminderUseCase.createParams(it.id, !isReminderSet))
                        val response = playChannelReminderUseCase.executeOnBackground()
                        status = PlayChannelReminderUseCase.checkRequestSuccess(response)
                    }

                    if(!status) failedRemindMe()
                    else {
                        _upcomingInfo.setValue { copy(isReminderSet = !isReminderSet) }
                        _upcomingState.emit(PlayUpcomingState.ReminderStatus(isReminded = isReminderSet))

                        _uiEvent.emit(PlayUpcomingUiEvent.RemindMeEvent(message = UiString.Resource(
                            if (!isReminderSet) R.string.play_cancel_remind_me_success else R.string.play_remind_me_success),
                            isSuccess = status))
                    }
                } ?: failedRemindMe()
            }) {
                failedRemindMe()
            }
        }


    }

    private fun handleWatchNowUpcomingChannel() {
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
        if(isFromLogin) updatePartnerInfo(_partnerInfo.value)
        if (_partnerInfo.value.status !is PlayPartnerFollowStatus.NotFollowable) {
           doFollowUnfollow(shouldForceFollow = isFromLogin) ?: return@needLogin
        }
    }

    private fun handleClickPartnerName(appLink: String) {
        viewModelScope.launch {
            _uiEvent.emit(PlayUpcomingUiEvent.OpenPageEvent(appLink))
        }
    }

    private fun doFollowUnfollow(shouldForceFollow: Boolean): PartnerFollowAction? {
        val channelData = mChannelData ?: return null
        val shopId = channelData.partnerInfo.id

        val followStatus = _partnerInfo.value.status as? PlayPartnerFollowStatus.Followable ?: return null
        val shouldFollow = if (shouldForceFollow) true else followStatus.followStatus == PartnerFollowableStatus.NotFollowed
        val followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow

        _partnerInfo.setValue { (copy(isLoadingFollow = true)) }

        viewModelScope.launchCatchError(block = {
            val isFollowing: Boolean = if(channelData.partnerInfo.type == PartnerType.Shop){
                repo.postFollowStatus(
                    shopId = shopId.toString(),
                    followAction = followAction,
                )
            } else {
                val data = repo.postFollowKol(followedKol = _observableKolId.value.toString(), followAction = followAction)
                if(data) followAction == PartnerFollowAction.Follow else false
            }
            _partnerInfo.setValue {
                val result = if(isFollowing) PartnerFollowableStatus.Followed else PartnerFollowableStatus.NotFollowed
                copy(isLoadingFollow = false, status = PlayPartnerFollowStatus.Followable(result))
            }
        }) {
            _uiEvent.emit(PlayUpcomingUiEvent.ShowError(it))
        }
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
            _uiEvent.emit(
                PlayUpcomingUiEvent.SaveTemporarySharingImage(imageUrl = _channelDetail.value.channelInfo.coverUrl)
            )
        }
    }

    private fun handleOpenSharingOption(isScreenshot: Boolean) {
        viewModelScope.launch {
            if(playShareExperience.isCustomSharingAllow()) {
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

    private fun handleSharingOption(shareModel: ShareModel) {
        viewModelScope.launch {
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
                    _upcomingState.value = PlayUpcomingState.ReminderStatus(isReminded = isReminderSet)
                }
            }
            return
        }

        when (requestCode) {
            REQUEST_CODE_LOGIN_REMIND_ME -> handleRemindMeUpcomingChannel()
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
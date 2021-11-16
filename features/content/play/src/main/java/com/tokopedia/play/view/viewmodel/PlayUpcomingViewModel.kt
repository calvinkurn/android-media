package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
): ViewModel() {

    private var mChannelId: String = ""

    private var mChannelData: PlayChannelData? = null

    val channelType: PlayChannelType
        get() = _channelDetail.value.channelInfo.channelType

    val isReminderSet: Boolean
        get() = _upcomingInfo.value.isReminderSet

    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 50)

    private val _channelDetail = MutableStateFlow(PlayChannelDetailUiModel())
    private val _partnerInfo = MutableStateFlow(PlayPartnerInfo())
    private val _upcomingInfo = MutableStateFlow(PlayUpcomingUiModel())
    private val _upcomingState = MutableStateFlow<PlayUpcomingState>(PlayUpcomingState.Unknown)

    private val _partnerUiState = _partnerInfo.map {
        PlayPartnerUiState(it.name, it.status)
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
        PlayShareUiState(shouldShow = it.shareInfo.shouldShow)
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

    val uiEvent: Flow<PlayViewerNewUiEvent>
        get() = _uiEvent

    private var sseJob: Job? = null

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
            _upcomingState.emit(
                when {
                    upcomingInfo.isAlreadyLive -> PlayUpcomingState.WatchNow
                    upcomingInfo.isReminderSet -> PlayUpcomingState.Reminded
                    else -> PlayUpcomingState.RemindMe
                }
            )
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

                    _upcomingState.emit(PlayUpcomingState.Unknown)

                    //TODO: emit event that has action button on the toaster
                    _uiEvent.emit(
                        ShowInfoEvent(
                            UiString.Resource(R.string.play_upcoming_channel_not_started)
                        )
                    )
                }
            }

            _upcomingInfo.setValue { copy(refreshWaitingDuration = playUiModelMapper.mapWaitingDuration(channelStatus)) }
        }, onError = {
            if(withAction) _upcomingState.emit(PlayUpcomingState.Refresh)

            _upcomingInfo.setValue { copy(refreshWaitingDuration = PlayUpcomingUiModel.REFRESH_WAITING_DURATION) }

            //TODO: ask PO when failed getting status
            _uiEvent.emit(ShowErrorEvent(it))
        })
    }

    private fun updatePartnerInfo(partnerInfo: PlayPartnerInfo) {
        if (partnerInfo.type == PartnerType.Shop && partnerInfo.id.toString() != userSession.shopId) {
            viewModelScope.launchCatchError(block = {
                val isFollowing = repo.getIsFollowingPartner(partnerId = partnerInfo.id)
                _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(isFollowing)) }
            }, onError = {

            })
        } else {
            _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.NotFollowable) }
        }
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
            ClickShareUpcomingAction -> handleClickShare()
            is OpenUpcomingPageResultAction -> handleOpenPageResult(action.isSuccess, action.requestCode)
        }
    }

    private fun handleImpressUpcomingChannel() {
        playAnalytic.impressUpcomingPage(mChannelId)
    }

    private fun handleClickUpcomingButton() {
        when(_upcomingState.value) {
            PlayUpcomingState.WatchNow -> handleWatchNowUpcomingChannel()
            PlayUpcomingState.RemindMe -> handleRemindMeUpcomingChannel(userClick = true)
            PlayUpcomingState.Refresh -> handleRefreshUpcomingChannel()
            else -> {}
        }
        _upcomingState.value = PlayUpcomingState.Loading
    }

    private fun handleRemindMeUpcomingChannel(userClick: Boolean)  {
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

                    _upcomingState.emit(PlayUpcomingState.Reminded)
                    _upcomingInfo.setValue { copy(isReminderSet = status) }

                    _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_remind_me_success), isSuccess = status))

                } ?: run {
                    _upcomingState.emit(PlayUpcomingState.RemindMe)
                    _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
                }
            }) {
                _upcomingState.emit(PlayUpcomingState.RemindMe)
                _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
            }
        }
    }

    private fun handleWatchNowUpcomingChannel() {
        playAnalytic.clickWatchNow(mChannelId)
        stopSSE()

        viewModelScope.launch {
            _uiEvent.emit(RefreshChannel)
        }
    }

    private fun handleRefreshUpcomingChannel() {
        updateStatusInfo(mChannelId)
    }

    private fun handleUpcomingTimerFinish() {
        viewModelScope.launch {
            _upcomingState.emit(PlayUpcomingState.Unknown)
            _uiEvent.emit(
                ShowInfoEvent(
                    UiString.Resource(R.string.play_upcoming_channel_not_started)
                )
            )
        }
        performRefreshWaitingDuration()
    }

    private fun performRefreshWaitingDuration() {
        viewModelScope.launchCatchError(dispatchers.computation, block = {
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
        playAnalytic.clickFollowShop(mChannelId, channelType, shopId.toString(), action.value)
    }

    private fun handleClickPartnerName() {
        viewModelScope.launch {
            val partnerInfo = _partnerInfo.value

            when (partnerInfo.type) {
                PartnerType.Shop -> {
                    playAnalytic.clickShop(mChannelId, channelType, partnerInfo.id.toString())
                    _uiEvent.emit(OpenPageEvent(ApplinkConst.SHOP, listOf(partnerInfo.id.toString()), pipMode = true))
                }
                PartnerType.Buyer -> _uiEvent.emit(OpenPageEvent(ApplinkConst.PROFILE, listOf(partnerInfo.id.toString()), pipMode = true))
                else -> {}
            }
        }
    }

    private fun handleClickShare() {
        val shareInfo = _channelDetail.value.shareInfo

        viewModelScope.launch {
            _uiEvent.emit(
                CopyToClipboardEvent(shareInfo.content)
            )

            _uiEvent.emit(
                ShowInfoEvent(
                    UiString.Resource(R.string.play_link_copied)
                )
            )
        }
    }

    private fun doFollowUnfollow(shouldForceFollow: Boolean): PartnerFollowAction? {
        val channelData = mChannelData ?: return null
        val shopId = channelData.partnerInfo.id

        val followStatus = _partnerInfo.value.status as? PlayPartnerFollowStatus.Followable ?: return null
        val shouldFollow = if (shouldForceFollow) true else !followStatus.isFollowing
        val followAction = if (shouldFollow) PartnerFollowAction.Follow else PartnerFollowAction.UnFollow

        _partnerInfo.setValue { copy(status = PlayPartnerFollowStatus.Followable(shouldFollow)) }

        viewModelScope.launchCatchError(block = {
            repo.postFollowStatus(
                shopId = shopId.toString(),
                followAction = followAction,
            )
        }) {}

        return followAction
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
                    OpenPageEvent(
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

    companion object {
        const val REQUEST_CODE_LOGIN_REMIND_ME = 678
        const val REQUEST_CODE_LOGIN_FOLLOW = 679
    }
}
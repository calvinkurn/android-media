package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.MutableLiveData
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
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.event.PlayViewerNewUiEvent
import com.tokopedia.play.view.uimodel.event.RemindMeEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayStatusInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.updateParams
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.sse.PlayChannelSSEPageSource
import com.tokopedia.play_common.sse.model.SSEAction
import com.tokopedia.play_common.sse.model.SSECloseReason
import com.tokopedia.play_common.sse.model.SSEResponse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
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

    private val _uiEvent = MutableSharedFlow<PlayViewerNewUiEvent>(extraBufferCapacity = 50)

    private val _observableUpcomingInfo = MutableLiveData<PlayUpcomingUiModel>()
    private val _observableStatusInfo = MutableLiveData<PlayStatusInfoUiModel>()

    private var sseJob: Job? = null

    val latestCompleteChannelData: PlayChannelData
        get() {
            val channelData = mChannelData ?: error("Channel Data should not be null")

            return channelData.copy(
                partnerInfo = channelData.partnerInfo,
                upcomingInfo = _observableUpcomingInfo.value ?: channelData.upcomingInfo
            )
        }

    fun initPage(channelId: String) {
        this.mChannelId = channelId
    }

    fun focusPage(channelData: PlayChannelData) {
        this.mChannelData = channelData

        _observableUpcomingInfo.value = channelData.upcomingInfo
        updateStatusInfo(mChannelId)
        startSSE(mChannelId)
    }

    override fun onCleared() {
        super.onCleared()
        stopSSE()
    }

    private fun updateStatusInfo(channelId: String) {
        viewModelScope.launchCatchError(block = {
            val channelStatus = getChannelStatus(channelId)
            _observableStatusInfo.value = _observableStatusInfo.value?.copy(
                statusType = playUiModelMapper.mapStatus(channelStatus),
                shouldAutoSwipeOnFreeze = false,
                waitingDuration = playUiModelMapper.mapWaitingDuration(channelStatus)
            )
        }, onError = {

        })
    }

    private suspend fun getChannelStatus(channelId: String) = withContext(dispatchers.io) {
        getChannelStatusUseCase.apply {
            setRequestParams(GetChannelStatusUseCase.createParams(arrayOf(channelId)))
        }.executeOnBackground()
    }

    fun submitAction(action: PlayUpcomingAction) {
        when(action) {
            ImpressUpcomingChannel -> handleImpressUpcomingChannel()
            ClickRemindMeUpcomingChannel -> handleRemindMeUpcomingChannel(userClick = true)
            ClickWatchNowUpcomingChannel -> handleWatchNowUpcomingChannel()
            UpcomingTimerFinish -> handleUpcomingTimerFinish()
            else -> {}
        }
    }

    private fun handleImpressUpcomingChannel() {
        playAnalytic.impressUpcomingPage(mChannelId)
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

                    _observableUpcomingInfo.value = _observableUpcomingInfo.value?.copy(isReminderSet = status)

                    _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_remind_me_success), isSuccess = status))

                } ?: _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
            }) {
                _uiEvent.emit(RemindMeEvent(message = UiString.Resource(R.string.play_failed_remind_me), isSuccess = false))
            }
        }
    }

    private fun handleWatchNowUpcomingChannel() {
        playAnalytic.clickWatchNow(mChannelId)
        stopSSE()
    }

    private fun handleUpcomingTimerFinish() {
        CoroutineScope(dispatchers.computation).launch {
//            delay(refreshWaitingDuration)

            val isAlreadyLive = _observableUpcomingInfo.value?.isAlreadyLive ?: false
            if(!isAlreadyLive) {
                // TODO: Send event to PlayUpcomingFragment
            }
        }
    }

    /**
     * SSE
     */
    private fun startSSE(channelId: String) {
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

    private fun handleUpdateChannelStatus(changedChannelId: String, currentChannelId: String) {
        if(changedChannelId == currentChannelId) {
            _observableUpcomingInfo.value = _observableUpcomingInfo.value?.copy(isAlreadyLive = true)
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
        if (!isSuccess) return
        when (requestCode) {
            REQUEST_CODE_LOGIN_REMIND_ME -> handleRemindMeUpcomingChannel(userClick = false)
            else -> {}
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN_REMIND_ME = 678
    }
}
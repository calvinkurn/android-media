package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.PlayUpcomingUiModel
import com.tokopedia.play.view.uimodel.action.ImpressUpcomingChannel
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.recom.PlayPinnedInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.updateParams
import com.tokopedia.play_common.sse.PlayChannelSSE
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2021
 */
class PlayUpcomingViewModel @Inject constructor(
    private val getChannelStatusUseCase: GetChannelStatusUseCase,
    private val playAnalytic: PlayNewAnalytic,
    private val playChannelSSE: PlayChannelSSE,
    private val repo: PlayViewerRepository,
): ViewModel() {

    private var mChannelId: String = ""

    private var mChannelData: PlayChannelData? = null

    private val _observableUpcomingInfo = MutableLiveData<PlayUpcomingUiModel>()

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
        _observableUpcomingInfo.value = channelData.upcomingInfo
    }

    fun submitAction(action: PlayViewerNewAction) {
        when(action) {
            ImpressUpcomingChannel -> handleImpressUpcomingChannel()
            else -> {}
        }
    }

    private fun handleImpressUpcomingChannel() {
        playAnalytic.impressUpcomingPage(mChannelId)
    }
}
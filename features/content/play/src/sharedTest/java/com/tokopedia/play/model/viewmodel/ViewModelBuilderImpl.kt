package com.tokopedia.play.model.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.domain.GetReportSummariesUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.mapper.MapperBuilder
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.util.logger.PlayLog
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.timer.TimerFactory
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
class ViewModelBuilderImpl(
    private val mapperBuilder: MapperBuilder,
) {

    fun buildPlayViewModel(
        channelId: String = "1",
        playVideoBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true),
        videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true),
        channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true),
        videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true),
        getReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true),
        userSession: UserSessionInterface = mockk(relaxed = true),
        socketMapper: PlaySocketToModelMapper = mapperBuilder.createSocketMapper(
            userSession = userSession,
        ),
        uiMapper: PlayUiModelMapper = mapperBuilder.createUiMapper(
            userSession = userSession,
        ),
        dispatchers: CoroutineDispatchers,
        remoteConfig: RemoteConfig = mockk(relaxed = true),
        playPreference: PlayPreference = mockk(relaxed = true),
        videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring = mockk(relaxed = true),
        playChannelWebSocket: PlayWebSocket = mockk(relaxed = true),
        repo: PlayViewerRepository = mockk(relaxed = true),
        playAnalytic: PlayNewAnalytic = mockk(relaxed = true),
        timerFactory: TimerFactory = mockk(relaxed = true),
        castPlayerHelper: CastPlayerHelper = mockk(relaxed = true),
        playShareExperience: PlayShareExperience = mockk(relaxed = true),
        playLog: PlayLog = mockk(relaxed = true),
        chatManagerFactory: ChatManager.Factory = mockk(relaxed = true),
        chatStreamsFactory: ChatStreams.Factory = mockk(relaxed = true),
        liveRoomMetricsCommon : PlayLiveRoomMetricsCommon = mockk(relaxed = true),
    ) = PlayViewModel(
        channelId,
        playVideoBuilder,
        videoStateProcessorFactory,
        channelStateProcessorFactory,
        videoBufferGovernorFactory,
        getReportSummariesUseCase,
        socketMapper,
        uiMapper,
        userSession,
        dispatchers,
        remoteConfig,
        playPreference,
        videoLatencyPerformanceMonitoring,
        playChannelWebSocket,
        repo,
        playAnalytic,
        timerFactory,
        castPlayerHelper,
        playShareExperience,
        playLog,
        chatManagerFactory,
        chatStreamsFactory,
        liveRoomMetricsCommon,
    )
}

package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.play.domain.repository.PlayViewerBroTrackerRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerBroTrackerRepositoryImpl @Inject constructor(
    private val broadcasterReportTrackViewerUseCase: BroadcasterReportTrackViewerUseCase,
    private val trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    private val dispatchers: CoroutineDispatchers
) : PlayViewerBroTrackerRepository {

    override suspend fun trackProducts(
        channelId: String,
        productIds: List<String>
    ) {
        withContext(dispatchers.io) {
            broadcasterReportTrackViewerUseCase.apply {
                params = BroadcasterReportTrackViewerUseCase.createParams(
                    channelId,
                    productIds
                )
            }.executeOnBackground()
        }
    }

    override suspend fun trackVisitChannel(
        channelId: String,
        sourceType: String
    ) {
        withContext(dispatchers.io) {
            trackVisitChannelBroadcasterUseCase.apply {
                setRequestParams(
                    TrackVisitChannelBroadcasterUseCase.createParams(
                        channelId,
                        sourceType
                    )
                )
            }.executeOnBackground()
        }
    }
}

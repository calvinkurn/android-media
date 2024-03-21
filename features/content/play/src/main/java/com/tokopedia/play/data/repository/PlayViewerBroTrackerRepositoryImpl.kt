package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.types.TrackContentType
import com.tokopedia.content.common.usecase.BroadcasterReportTrackViewerUseCase
import com.tokopedia.play.domain.repository.PlayViewerBroTrackerRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerBroTrackerRepositoryImpl @Inject constructor(
    private val broadcasterReportTrackViewerUseCase: BroadcasterReportTrackViewerUseCase,
    private val dispatchers: CoroutineDispatchers
) : PlayViewerBroTrackerRepository {

    private suspend fun trackEvent(
        channelId: String,
        productIds: List<String>, event: BroadcasterReportTrackViewerUseCase.Companion.Event
    ) {
        withContext(dispatchers.io) {
            broadcasterReportTrackViewerUseCase.apply {
                params = BroadcasterReportTrackViewerUseCase.createParams(
                    channelId = channelId,
                    productIds = productIds,
                    event = event,
                    type = TrackContentType.Play
                )
            }.executeOnBackground()
        }
    }

    override suspend fun trackProducts(
        channelId: String,
        productIds: List<String>
    ) {
        trackEvent(
            channelId,
            productIds,
            BroadcasterReportTrackViewerUseCase.Companion.Event.ProductChanges,
        )
    }

    override suspend fun trackVisitChannel(
        channelId: String,
        sourceType: String
    ) {
        trackEvent(
            channelId,
            emptyList(),
            BroadcasterReportTrackViewerUseCase.Companion.Event.Visit,
        )
    }
}

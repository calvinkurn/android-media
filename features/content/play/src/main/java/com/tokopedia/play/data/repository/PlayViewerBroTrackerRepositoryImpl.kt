package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.TrackProductTagBroadcasterUseCase
import com.tokopedia.play.domain.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.play.domain.repository.PlayViewerBroTrackerRepository
import com.tokopedia.play.view.type.PlaySource
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayViewerBroTrackerRepositoryImpl @Inject constructor(
    private val trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
    private val trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    private val dispatchers: CoroutineDispatchers,
) : PlayViewerBroTrackerRepository {

    override suspend fun trackProducts(
        channelId: String,
        productIds: List<String>
    ) {
        withContext(dispatchers.io) {
            trackProductTagBroadcasterUseCase.apply {
                params = TrackProductTagBroadcasterUseCase.createParams(
                    channelId,
                    productIds
                )
            }.executeOnBackground()
        }
    }

    override suspend fun trackVisitChannel(
        channelId: String,
        source: PlaySource,
    ) {
        withContext(dispatchers.io) {
            trackVisitChannelBroadcasterUseCase.apply {
                setRequestParams(
                    TrackVisitChannelBroadcasterUseCase.createParams(
                        channelId, source.key
                    )
                )
            }.executeOnBackground()
        }
    }
}
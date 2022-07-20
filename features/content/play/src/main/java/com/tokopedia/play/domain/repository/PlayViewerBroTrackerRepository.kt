package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.type.PlaySource

interface PlayViewerBroTrackerRepository {

    suspend fun trackProducts(
        channelId: String,
        productIds: List<String>,
    )

    suspend fun trackVisitChannel(
        channelId: String,
        source: PlaySource,
    )
}
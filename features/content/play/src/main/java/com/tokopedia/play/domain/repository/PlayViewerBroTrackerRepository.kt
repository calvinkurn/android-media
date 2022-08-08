package com.tokopedia.play.domain.repository


interface PlayViewerBroTrackerRepository {

    suspend fun trackProducts(
        channelId: String,
        productIds: List<String>,
    )

    suspend fun trackVisitChannel(
        channelId: String,
        sourceType: String,
    )
}
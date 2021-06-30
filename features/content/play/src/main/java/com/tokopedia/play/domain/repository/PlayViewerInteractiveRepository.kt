package com.tokopedia.play.domain.repository

import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel

/**
 * Created by jegul on 30/06/21
 */
interface PlayViewerInteractiveRepository {

    suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel

    suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean
}
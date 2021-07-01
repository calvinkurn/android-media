package com.tokopedia.play.domain.repository

import com.tokopedia.play.data.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage

/**
 * Created by jegul on 30/06/21
 */
interface PlayViewerInteractiveRepository : PlayInteractiveStorage {

    suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel

    suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean
}
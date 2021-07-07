package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play_common.model.dto.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by jegul on 30/06/21
 */
interface PlayViewerInteractiveRepository : PlayInteractiveStorage {

    suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel

    suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean

    suspend fun getInteractiveLeaderboard(channelId: String): PlayLeaderboardInfoUiModel
}
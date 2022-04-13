package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by jegul on 30/06/21
 */
interface PlayViewerInteractiveRepository : PlayInteractiveStorage {

    suspend fun getCurrentInteractive(channelId: String): InteractiveUiModel

    suspend fun postInteractiveTap(channelId: String, interactiveId: String): Boolean

    suspend fun getInteractiveLeaderboard(channelId: String): PlayLeaderboardInfoUiModel
}
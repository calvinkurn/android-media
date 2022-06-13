package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSessionUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel

/**
 * Created by meyta.taliti on 09/12/21.
 */
interface PlayBroadcastInteractiveRepository {

    suspend fun getInteractiveConfig(): InteractiveConfigUiModel

    suspend fun getCurrentInteractive(channelId: String): PlayCurrentInteractiveModel

    suspend fun getInteractiveLeaderboard(channelId: String, isChatAllowed: () -> Boolean): PlayLeaderboardInfoUiModel

    suspend fun createInteractiveSession(channelId: String,
                                         title: String,
                                         durationInMs: Long): InteractiveSessionUiModel
}
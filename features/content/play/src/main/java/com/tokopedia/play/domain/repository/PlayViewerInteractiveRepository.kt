package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel

/**
 * Created by jegul on 30/06/21
 */
interface PlayViewerInteractiveRepository : PlayInteractiveStorage {

    suspend fun getCurrentInteractive(channelId: String): GameUiModel

    suspend fun postGiveawayTap(channelId: String, interactiveId: String): Boolean

    suspend fun getInteractiveLeaderboard(channelId: String): List<LeaderboardGameUiModel>

    suspend fun answerQuiz(interactiveId: String, choiceId: String): String
}

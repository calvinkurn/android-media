package com.tokopedia.play_common.ui.leaderboard.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayEngagementLeaderboardViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementLeaderboardAdapterDelegate(
    private val leaderboardWinnerListener: PlayEngagementLeaderboardViewHolder.Listener
) : TypedAdapterDelegate<PlayLeaderboardUiModel, PlayLeaderboardUiModel, PlayEngagementLeaderboardViewHolder>(PlayEngagementLeaderboardViewHolder.LAYOUT) {

    override fun onBindViewHolder(
        item: PlayLeaderboardUiModel,
        holder: PlayEngagementLeaderboardViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayEngagementLeaderboardViewHolder {
        return PlayEngagementLeaderboardViewHolder(basicView, leaderboardWinnerListener)
    }


}
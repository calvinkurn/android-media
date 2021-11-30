package com.tokopedia.play_common.ui.leaderboard.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveLeaderboardAdapterDelegate(
    private val leaderboardWinnerListener: PlayInteractiveLeaderboardViewHolder.Listener
) : TypedAdapterDelegate<PlayLeaderboardUiModel, PlayLeaderboardUiModel, PlayInteractiveLeaderboardViewHolder>(PlayInteractiveLeaderboardViewHolder.LAYOUT) {

    override fun onBindViewHolder(
        item: PlayLeaderboardUiModel,
        holder: PlayInteractiveLeaderboardViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayInteractiveLeaderboardViewHolder {
        return PlayInteractiveLeaderboardViewHolder(basicView, leaderboardWinnerListener)
    }


}
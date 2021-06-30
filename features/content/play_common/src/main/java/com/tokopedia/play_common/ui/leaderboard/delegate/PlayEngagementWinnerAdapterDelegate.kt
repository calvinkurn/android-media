package com.tokopedia.play_common.ui.leaderboard.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayEngagementWinnerViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementWinnerAdapterDelegate(
    private val winnerListener: PlayEngagementWinnerViewHolder.Listener
) : TypedAdapterDelegate<PlayWinnerUiModel, PlayWinnerUiModel, PlayEngagementWinnerViewHolder>(PlayEngagementWinnerViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayWinnerUiModel, holder: PlayEngagementWinnerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayEngagementWinnerViewHolder {
        return PlayEngagementWinnerViewHolder(basicView, winnerListener)
    }

}
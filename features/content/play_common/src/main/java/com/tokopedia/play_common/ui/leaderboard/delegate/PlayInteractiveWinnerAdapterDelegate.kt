package com.tokopedia.play_common.ui.leaderboard.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveWinnerViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveWinnerAdapterDelegate(
    private val winnerListener: PlayInteractiveWinnerViewHolder.Listener
) : TypedAdapterDelegate<PlayWinnerUiModel, PlayWinnerUiModel, PlayInteractiveWinnerViewHolder>(PlayInteractiveWinnerViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayWinnerUiModel, holder: PlayInteractiveWinnerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): PlayInteractiveWinnerViewHolder {
        return PlayInteractiveWinnerViewHolder(basicView, winnerListener)
    }

}
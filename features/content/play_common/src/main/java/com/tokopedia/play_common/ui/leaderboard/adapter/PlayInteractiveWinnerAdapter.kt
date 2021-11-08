package com.tokopedia.play_common.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.delegate.PlayInteractiveWinnerAdapterDelegate
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveWinnerViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveWinnerAdapter(winnerListener: PlayInteractiveWinnerViewHolder.Listener) : BaseDiffUtilAdapter<PlayWinnerUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayInteractiveWinnerAdapterDelegate(winnerListener))
    }

    override fun areItemsTheSame(oldItem: PlayWinnerUiModel, newItem: PlayWinnerUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayWinnerUiModel,
        newItem: PlayWinnerUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
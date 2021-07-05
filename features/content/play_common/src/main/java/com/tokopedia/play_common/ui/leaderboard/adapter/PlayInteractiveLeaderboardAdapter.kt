package com.tokopedia.play_common.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.ui.leaderboard.delegate.PlayInteractiveLeaderboardAdapterDelegate
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveLeaderboardAdapter(
    leaderboardWinnerListener: PlayInteractiveLeaderboardViewHolder.Listener
) : BaseAdapter<PlayLeaderboardUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayInteractiveLeaderboardAdapterDelegate(leaderboardWinnerListener))
    }

}
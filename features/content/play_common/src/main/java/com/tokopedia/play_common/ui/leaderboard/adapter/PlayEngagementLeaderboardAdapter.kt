package com.tokopedia.play_common.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.ui.leaderboard.delegate.PlayEngagementLeaderboardAdapterDelegate
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayEngagementLeaderboardViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementLeaderboardAdapter(
    leaderboardWinnerListener: PlayEngagementLeaderboardViewHolder.Listener
) : BaseAdapter<PlayLeaderboardUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayEngagementLeaderboardAdapterDelegate(leaderboardWinnerListener))
    }

}
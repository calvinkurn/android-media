package com.tokopedia.play_common.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.delegate.PlayEngagementWinnerAdapterDelegate
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayEngagementWinnerViewHolder


/**
 * Created by mzennis on 30/06/21.
 */
class PlayEngagementWinnerAdapter(winnerListener: PlayEngagementWinnerViewHolder.Listener) : BaseAdapter<PlayWinnerUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayEngagementWinnerAdapterDelegate(winnerListener))
    }
}
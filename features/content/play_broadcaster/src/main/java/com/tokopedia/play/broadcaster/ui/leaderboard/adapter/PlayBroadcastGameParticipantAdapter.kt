package com.tokopedia.play.broadcaster.ui.leaderboard.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.leaderboard.delegate.PlayBroadcastGameParticipantAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel

class PlayBroadcastGameParticipantAdapter() :
    BaseDiffUtilAdapter<GameParticipantUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayBroadcastGameParticipantAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: GameParticipantUiModel, newItem: GameParticipantUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: GameParticipantUiModel,
        newItem: GameParticipantUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
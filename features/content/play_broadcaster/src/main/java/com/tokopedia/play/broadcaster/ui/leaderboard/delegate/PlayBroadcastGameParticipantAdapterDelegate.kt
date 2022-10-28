package com.tokopedia.play.broadcaster.ui.leaderboard.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.game.GameParticipantViewHolder

class PlayBroadcastGameParticipantAdapterDelegate : TypedAdapterDelegate<
        GameParticipantUiModel,
        GameParticipantUiModel,
        GameParticipantViewHolder>(
    GameParticipantViewHolder.LAYOUT
) {

    override fun onBindViewHolder(
        item: GameParticipantUiModel,
        holder: GameParticipantViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): GameParticipantViewHolder {
        return GameParticipantViewHolder(basicView)
    }
}

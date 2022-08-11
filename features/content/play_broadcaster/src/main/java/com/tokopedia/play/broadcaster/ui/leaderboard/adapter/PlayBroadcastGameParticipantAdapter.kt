package com.tokopedia.play.broadcaster.ui.leaderboard.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.leaderboard.delegate.PlayBroadcastGameParticipantAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.game.GameParticipantUiModel

class PlayBroadcastGameParticipantAdapter(private val loadMoreParticipant: () -> Unit) :
    BaseDiffUtilAdapter<GameParticipantUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayBroadcastGameParticipantAdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: GameParticipantUiModel,
        newItem: GameParticipantUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: GameParticipantUiModel,
        newItem: GameParticipantUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == (itemCount - 1)) loadMoreParticipant()
    }
}
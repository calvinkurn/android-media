package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.viewholder.game.SelectGameViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.GameAdapterDelegate

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class SelectGameAdapter(
    listener: SelectGameViewHolder.Listener,
) : BaseDiffUtilAdapter<GameType>(){

    init {
        delegatesManager
            .addDelegate(GameAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: GameType, newItem: GameType): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: GameType, newItem: GameType): Boolean {
        return oldItem == newItem
    }
}
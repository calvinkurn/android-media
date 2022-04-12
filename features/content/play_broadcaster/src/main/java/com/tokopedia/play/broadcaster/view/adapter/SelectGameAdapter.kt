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
) : BaseDiffUtilAdapter<SelectGameAdapter.Model>(){

    init {
        delegatesManager
            .addDelegate(GameAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
        if(oldItem is Model.Item && newItem is Model.Item)
            return oldItem.gameType == newItem.gameType
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
        return oldItem == newItem
    }

    sealed class Model {
        data class Item(val gameType: GameType): Model()
    }
}
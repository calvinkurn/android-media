package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.viewholder.game.SelectGameViewHolder
import com.tokopedia.play_common.R as commonR

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class GameAdapterDelegate(
    private val listener: SelectGameViewHolder.Listener,
) : TypedAdapterDelegate<GameType, GameType, SelectGameViewHolder>(commonR.layout.view_play_empty) {

    override fun onBindViewHolder(
        item: GameType,
        holder: SelectGameViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SelectGameViewHolder {
        return SelectGameViewHolder.create(parent, listener)
    }
}
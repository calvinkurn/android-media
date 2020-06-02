package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.PlayEtalaseUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseAdapterDelegate(
        private val listener: PlayEtalaseViewHolder.Listener
) : TypedAdapterDelegate<PlayEtalaseUiModel, Any, PlayEtalaseViewHolder>(PlayEtalaseViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: PlayEtalaseUiModel, holder: PlayEtalaseViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayEtalaseViewHolder {
        return PlayEtalaseViewHolder(basicView, listener)
    }
}
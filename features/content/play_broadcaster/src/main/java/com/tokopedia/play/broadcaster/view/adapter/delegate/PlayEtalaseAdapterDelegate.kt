package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel

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
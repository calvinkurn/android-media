package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayEtalaseAdapterDelegate
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseAdapter(
        etalaseListener: PlayEtalaseViewHolder.Listener
) : BaseDiffUtilAdapter<PlayEtalaseUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayEtalaseAdapterDelegate(etalaseListener))
    }

    override fun areItemsTheSame(oldItem: PlayEtalaseUiModel, newItem: PlayEtalaseUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlayEtalaseUiModel, newItem: PlayEtalaseUiModel): Boolean {
        return oldItem == newItem
    }
}
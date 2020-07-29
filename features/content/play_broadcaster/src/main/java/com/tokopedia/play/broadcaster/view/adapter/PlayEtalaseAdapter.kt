package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.EtalaseContentUiModel
import com.tokopedia.play.broadcaster.ui.model.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.EtalaseLoadingAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayEtalaseAdapterDelegate

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseAdapter(
        etalaseListener: PlayEtalaseViewHolder.Listener
) : BaseDiffUtilAdapter<EtalaseUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayEtalaseAdapterDelegate(etalaseListener))
                .addDelegate(EtalaseLoadingAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: EtalaseUiModel, newItem: EtalaseUiModel): Boolean {
        return if (oldItem is EtalaseContentUiModel && newItem is EtalaseContentUiModel) oldItem.id == newItem.id
        else false
    }

    override fun areContentsTheSame(oldItem: EtalaseUiModel, newItem: EtalaseUiModel): Boolean {
        return oldItem == newItem
    }
}
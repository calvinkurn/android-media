package com.tokopedia.play.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.adapter.delegate.PlayMoreActionAdapterDelegate
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionAdapter : BaseDiffUtilAdapter<PlayMoreActionUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayMoreActionAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayMoreActionUiModel, newItem: PlayMoreActionUiModel): Boolean {
        return oldItem.priority == newItem.priority
    }

    override fun areContentsTheSame(oldItem: PlayMoreActionUiModel, newItem: PlayMoreActionUiModel): Boolean {
        return oldItem == newItem
    }
}

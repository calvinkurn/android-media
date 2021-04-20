package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.placeholder.PlayWidgetCardPlaceholderAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetCardPlaceholderAdapter : BaseDiffUtilAdapter<PlayWidgetUiModel.Placeholder>() {

    init {
        delegatesManager.addDelegate(PlayWidgetCardPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayWidgetUiModel.Placeholder, newItem: PlayWidgetUiModel.Placeholder): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetUiModel.Placeholder, newItem: PlayWidgetUiModel.Placeholder): Boolean {
        return oldItem == newItem
    }
}
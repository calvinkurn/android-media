package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.placeholder.PlayWidgetCardPlaceholderAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetCardPlaceholderAdapter : BaseDiffUtilAdapter<Unit>() {

    init {
        delegatesManager.addDelegate(PlayWidgetCardPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: Unit, newItem: Unit): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Unit, newItem: Unit): Boolean {
        return oldItem == newItem
    }
}
package com.tokopedia.play.widget.sample

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSampleAdapter(
        widgetListener: PlayWidgetListener? = null
) : BaseDiffUtilAdapter<PlayWidgetUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
                .addDelegate(PlayWidgetViewAdapterDelegate(widgetListener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetUiModel, newItem: PlayWidgetUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetUiModel, newItem: PlayWidgetUiModel): Boolean {
        return oldItem == newItem
    }
}
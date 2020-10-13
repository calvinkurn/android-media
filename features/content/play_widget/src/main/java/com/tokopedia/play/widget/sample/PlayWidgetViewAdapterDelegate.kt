package com.tokopedia.play.widget.sample

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 10/10/20
 */
class PlayWidgetViewAdapterDelegate(
        private val widgetListener: PlayWidgetListener? = null
) : TypedAdapterDelegate<PlayWidgetUiModel, PlayWidgetUiModel, PlayWidgetViewHolder>(PlayWidgetViewHolder.layout) {

    override fun onBindViewHolder(item: PlayWidgetUiModel, holder: PlayWidgetViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetViewHolder {
        return PlayWidgetViewHolder(basicView, widgetListener)
    }
}
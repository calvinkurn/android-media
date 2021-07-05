package com.tokopedia.play.widget.ui.adapter.delegate.placeholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.placeholder.PlayWidgetCardPlaceholderViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetCardPlaceholderAdapterDelegate
    : TypedAdapterDelegate<PlayWidgetUiModel.Placeholder, PlayWidgetUiModel.Placeholder, PlayWidgetCardPlaceholderViewHolder>(
        PlayWidgetCardPlaceholderViewHolder.layout
) {

    override fun onBindViewHolder(item: PlayWidgetUiModel.Placeholder, holder: PlayWidgetCardPlaceholderViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardPlaceholderViewHolder {
        return PlayWidgetCardPlaceholderViewHolder(basicView)
    }
}
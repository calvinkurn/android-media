package com.tokopedia.play.widget.ui.adapter.delegate.placeholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.placeholder.PlayWidgetCardPlaceholderViewHolder

/**
 * Created by jegul on 12/10/20
 */
class PlayWidgetCardPlaceholderAdapterDelegate
    : TypedAdapterDelegate<Unit, Unit, PlayWidgetCardPlaceholderViewHolder>(
        PlayWidgetCardPlaceholderViewHolder.layout
) {

    override fun onBindViewHolder(item: Unit, holder: PlayWidgetCardPlaceholderViewHolder) {
        holder.bind()
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetCardPlaceholderViewHolder {
        return PlayWidgetCardPlaceholderViewHolder(basicView)
    }
}
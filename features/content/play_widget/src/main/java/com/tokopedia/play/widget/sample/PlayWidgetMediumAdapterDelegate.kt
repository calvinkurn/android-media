package com.tokopedia.play.widget.sample

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetMediumViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetMediumAdapterDelegate
    : TypedAdapterDelegate<PlayWidgetMediumUiModel, PlayWidgetUiModel, PlayWidgetMediumViewHolder>(PlayWidgetMediumViewHolder.layout) {

    override fun onBindViewHolder(item: PlayWidgetMediumUiModel, holder: PlayWidgetMediumViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetMediumViewHolder {
        return PlayWidgetMediumViewHolder(basicView)
    }
}
package com.tokopedia.play.widget.sample

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetSmallViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetSmallAdapterDelegate
    : TypedAdapterDelegate<PlayWidgetSmallUiModel, PlayWidgetUiModel, PlayWidgetSmallViewHolder>(PlayWidgetSmallViewHolder.layout) {

    override fun onBindViewHolder(item: PlayWidgetSmallUiModel, holder: PlayWidgetSmallViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PlayWidgetSmallViewHolder {
        return PlayWidgetSmallViewHolder(basicView)
    }
}
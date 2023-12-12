package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 28/11/23"
 * Project name: android-tokopedia-core
 **/

abstract class GWPCardViewHolder<in DATA : GWPWidgetUiModel.Card>(
    itemView: View,
    private val getParentTrackData: () -> ComponentTrackDataModel
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: DATA)

    protected fun getTrackData(data: DATA): GWPWidgetUiModel.CardTrackData {
        val trackDataModel = data.trackData.apply {
            parentTrackData = getParentTrackData
            position = bindingAdapterPosition.inc()
        }
        return data
    }
}

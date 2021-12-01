package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.listener.PdpPlayWidgetAnalyticListener

class ContentWidgetViewHolder(
    view: View,
    listener: DynamicProductDetailListener,
    private val playWidgetViewHolder: PlayWidgetViewHolder
) : ProductDetailPageViewHolder<ContentWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pdp_play_widget
    }

    private val playWidgetAnalyticListener = PdpPlayWidgetAnalyticListener(listener)

    init {
        playWidgetViewHolder.coordinator.setAnalyticListener(playWidgetAnalyticListener)
    }

    override fun bind(element: ContentWidgetDataModel) {
        playWidgetAnalyticListener.componentTrackDataModel = getComponentTrackData(element)
        playWidgetViewHolder.bind(element.playWidgetUiModel, this)
    }
}
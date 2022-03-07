package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
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

        private const val MINIMUM_ITEMS = 3
    }

    private val container: View? = view.findViewById(R.id.pdp_play_widget_container)
    private val playWidgetAnalyticListener = PdpPlayWidgetAnalyticListener(listener)

    init {
        playWidgetViewHolder.coordinator.setAnalyticListener(playWidgetAnalyticListener)
    }

    override fun bind(element: ContentWidgetDataModel) {
        val playWidgetUiModel = element.playWidgetUiModel
        if (playWidgetUiModel is PlayWidgetUiModel.Medium && playWidgetUiModel.items.size < MINIMUM_ITEMS) {
            hideComponent()
        } else showComponent()
        playWidgetAnalyticListener.componentTrackDataModel = getComponentTrackData(element)
        playWidgetViewHolder.bind(element.playWidgetUiModel, this)
    }

    private fun hideComponent() {
        container?.layoutParams?.height = 0
    }

    private fun showComponent() {
        container?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.sample.analytic.global.model.PlayWidgetPDPAnalyticModel
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel

class ContentWidgetViewHolder(
    view: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder
) : ProductDetailPageViewHolder<ContentWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pdp_play_widget

        private const val MINIMUM_ITEMS = 3
    }

    private val container: View? = view.findViewById(R.id.pdp_play_widget_container)

    init {
        playWidgetViewHolder.coordinator.setAnalyticModel(PlayWidgetPDPAnalyticModel())
    }

    override fun bind(element: ContentWidgetDataModel) {
        val playWidgetState = element.playWidgetState
        if (playWidgetState.model.items.size < MINIMUM_ITEMS) {
            hideComponent()
        } else showComponent()
        playWidgetViewHolder.bind(element.playWidgetState, this)
    }

    private fun hideComponent() {
        container?.layoutParams?.height = 0
    }

    private fun showComponent() {
        container?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.global.model.PlayWidgetPDPAnalyticModel
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.utils.extensions.addOnImpressionListener
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ContentWidgetViewHolder(
    view: View,
    private val playWidgetViewHolder: PlayWidgetViewHolder,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ContentWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_pdp_play_widget
    }
    init {
        playWidgetViewHolder.coordinator.setAnalyticModel(
            PlayWidgetPDPAnalyticModel(
                listener.getProductInfo()?.parentProductId.orEmpty()
            )
        )
    }

    override fun bind(element: ContentWidgetDataModel) {
        playWidgetViewHolder.bind(element.playWidgetState, this)
        itemView.addOnImpressionListener(
            holder = element.impressHolder,
            holders = listener.getImpressionHolders(),
            name = element.name,
            useHolders = listener.isRemoteCacheableActive()
        ) {
            listener.onImpressComponent(getComponentTrackData(element))
        }
    }
}

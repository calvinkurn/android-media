package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpRecommendationWidgetDataModel
import com.tokopedia.product.detail.databinding.ItemRecomViewHolderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel on 27/03/23
 */
class PdpRecommendationWidgetViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<PdpRecommendationWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_recom_view_holder
    }

    private val binding: ItemRecomViewHolderBinding? by viewBinding()

    override fun bind(element: PdpRecommendationWidgetDataModel) {
        element.recommendationWidgetModel.let {
            binding?.recomWidget?.bind(it)
            binding?.root?.addOnImpressionListener(element.impressHolder) {
                listener.onImpressComponent(
                    ComponentTrackDataModel(
                        componentType = element.type(),
                        componentName = element.name(),
                        adapterPosition = bindingAdapterPosition
                    )
                )
            }
        }
    }
}

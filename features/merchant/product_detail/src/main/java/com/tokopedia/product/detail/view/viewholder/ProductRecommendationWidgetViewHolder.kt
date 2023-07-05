package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationWidgetUiModel
import com.tokopedia.product.detail.databinding.ItemProductRecommendationWidgetBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ProductRecommendationWidgetViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : AbstractViewHolder<ProductRecommendationWidgetUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_recommendation_widget
    }

    private val binding = ItemProductRecommendationWidgetBinding.bind(view)

    override fun bind(element: ProductRecommendationWidgetUiModel) {
        binding.recomWidget.bind(element.recommendationWidget)
        binding.root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(
                ComponentTrackDataModel(
                    componentType = element.type(),
                    componentName = element.name(),
                    adapterPosition = bindingAdapterPosition
                )
            )
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding.recomWidget.recycle()
    }
}

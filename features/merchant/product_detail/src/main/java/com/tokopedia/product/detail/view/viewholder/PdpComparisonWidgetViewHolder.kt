package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.databinding.ItemComparisonWidgetViewholderBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel

class PdpComparisonWidgetViewHolder(
      private val view: View,
      private val listener: DynamicProductDetailListener)
: AbstractViewHolder<PdpComparisonWidgetDataModel>(view), ComparisonWidgetInterface {

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    private val binding = ItemComparisonWidgetViewholderBinding.bind(view)

    companion object {
        val LAYOUT = R.layout.item_comparison_widget_viewholder
        const val PDP_PAGE_NAME = "product detail page"
    }

    override fun bind(element: PdpComparisonWidgetDataModel) {
        binding.comparisonWidget.setComparisonWidgetData(
                element.recommendationWidget,
                this@PdpComparisonWidgetViewHolder,
                RecommendationTrackingModel(
                        eventClick = ProductTrackingConstant.PDP.EVENT_CLICK_PDP,
                        androidPageName = PDP_PAGE_NAME,
                        headerTitle = element.recommendationWidget.title,
                        eventCategory = PDP_PAGE_NAME
                ),
                listener.getFragmentTrackingQueue()
        )
        this.componentTrackDataModel = getComponentTrackData(element)
    }

    override fun onProductCardImpressed(recommendationItem: RecommendationItem, comparisonListModel: ComparisonListModel, position: Int) {
        val topAdsImageUrl = recommendationItem.trackerImageUrl
        if (recommendationItem.isTopAds) {
            listener.sendTopAdsImpression(
                    topAdsImageUrl,
                    recommendationItem.productId.toString(),
                    recommendationItem.name, recommendationItem.imageUrl)
        }
    }

    override fun onProductCardClicked(recommendationItem: RecommendationItem, comparisonListModel: ComparisonListModel, position: Int) {
        view.context?.run {
            RouteManager.route(this,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    recommendationItem.productId.toString())
        }
    }

    private fun getComponentTrackData(element: PdpComparisonWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}

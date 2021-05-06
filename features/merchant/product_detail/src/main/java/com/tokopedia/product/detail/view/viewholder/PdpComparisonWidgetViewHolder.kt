package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetMapper
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleModel
import kotlinx.android.synthetic.main.item_comparison_widget_viewholder.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PdpComparisonWidgetViewHolder(
      private val view: View,
      private val listener: DynamicProductDetailListener)
: AbstractViewHolder<PdpComparisonWidgetDataModel>(view), ComparisonWidgetInterface {

    private var componentTrackDataModel: ComponentTrackDataModel? = null

    companion object {
        val LAYOUT = R.layout.item_comparison_widget_viewholder
        const val PDP_PAGE_NAME = "product detail page"
    }

    override fun bind(element: PdpComparisonWidgetDataModel) {
        itemView.comparison_widget.setComparisonWidgetData(
                element.recommendationWidget,
                listener.getStickyTitleView(),
                this@PdpComparisonWidgetViewHolder,
                RecommendationTrackingModel(
                        androidPageName = PDP_PAGE_NAME,
                        headerTitle = element.recommendationWidget.title
                ),
                object : StickyTitleInterface {
                    override fun onStickyTitleClick(stickyTitleModel: StickyTitleModel) {
                        view.context?.run {
                            RouteManager.route(this,
                                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                                    stickyTitleModel.recommendationItem.productId.toString())
                        }
                    }

                    override fun onStickyTitleShow(isShowing: Boolean) {

                    }
                },
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

    override fun onSeeAllSpecClicked(comparisonListModel: ComparisonListModel) {

    }

    private fun getComponentTrackData(element: PdpComparisonWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)
}

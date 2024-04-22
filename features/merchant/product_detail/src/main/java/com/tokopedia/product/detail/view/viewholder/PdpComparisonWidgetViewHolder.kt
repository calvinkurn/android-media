package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductTrackingConstant
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpComparisonWidgetDataModel
import com.tokopedia.product.detail.databinding.ItemComparisonWidgetViewholderBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonListModel
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.RecommendationTrackingModel

class PdpComparisonWidgetViewHolder(
    private val view: View,
    private val listener: ProductDetailListener
)
: AbstractViewHolder<PdpComparisonWidgetDataModel>(view), ComparisonWidgetInterface, AdsItemClickListener, AdsViewListener {

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
            this@PdpComparisonWidgetViewHolder,
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

    override fun onAreaClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
    }

    override fun onProductImageClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
    }

    override fun onSellerInfoClicked(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
    }

    override fun onViewAttachedToWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int) {
        recomItem.sendShowAdsByteIo(itemView.context)
    }

    override fun onViewDetachedFromWindow(recomItem: RecommendationItem, bindingAdapterPosition: Int, visiblePercentage: Int) {
        recomItem.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
    }

    private fun getComponentTrackData(element: PdpComparisonWidgetDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}

package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetListener
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselWidgetView
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment.Companion.SOURCE

class HomeProductRecomViewHolder(
    itemView: View,
    private val tokoNowListener: TokoNowView? = null,
    private val listener: HomeProductRecomListener? = null
): AbstractViewHolder<HomeProductRecomUiModel>(itemView), RecommendationCarouselWidgetListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private val productRecom: RecommendationCarouselWidgetView by lazy { itemView.findViewById(R.id.carouselProductRecom) }

    private var channelId = ""
    private var isOoc = false

    override fun bind(element: HomeProductRecomUiModel) {
        channelId = element.id
        isOoc = element.id == PRODUCT_RECOM_OOC
        productRecom.bind(
            carouselData = RecommendationCarouselData(
                recommendationData = element.recomWidget,
                state = RecommendationCarouselData.STATE_READY,
            ),
            widgetListener = this
        )
        setOnScrollListener()
        restoreScrollState()
    }

    override fun onRecomBannerImpressed(data: RecommendationCarouselData, adapterPosition: Int) { /* nothing to do */ }

    override fun onRecomBannerClicked(data: RecommendationCarouselData, applink: String, adapterPosition: Int) { /* nothing to do */ }

    override fun onChannelWidgetEmpty() { /* nothing to do */ }

    override fun onChannelExpired(data: RecommendationCarouselData, channelPosition: Int) { /* nothing to do */ }

    override fun onRecomChannelImpressed(data: RecommendationCarouselData) { /* nothing to do */ }

    override fun onRecomProductCardImpressed(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardImpressed(data.recommendationData.recommendationItemList, channelId, data.recommendationData.title, data.recommendationData.pageName, isOoc)
    }

    override fun onSeeAllBannerClicked(
        data: RecommendationCarouselData,
        applink: String
    ) {
        listener?.onSeeAllBannerClicked(channelId, data.recommendationData.title, isOoc)
    }

    override fun onRecomProductCardClicked(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        applink: String,
        itemPosition: Int,
        adapterPosition: Int
    ) {
        listener?.onRecomProductCardClicked(recomItem, channelId, data.recommendationData.title, itemPosition.toString(), isOoc)
    }

    override fun onRecomProductCardAddToCartNonVariant(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int,
        quantity: Int
    ) {
        listener?.onProductRecomNonVariantClick(recomItem, quantity, data.recommendationData.title, channelId, adapterPosition.toString())
    }

    override fun onRecomProductCardAddVariantClick(
        data: RecommendationCarouselData,
        recomItem: RecommendationItem,
        adapterPosition: Int
    ) {
        AtcVariantHelper.goToAtcVariant(
            context = itemView.context,
            productId = recomItem.productId.toString(),
            pageSource = SOURCE,
            isTokoNow = true,
            shopId = recomItem.shopId.toString(),
            startActivitResult = (tokoNowListener?.getFragmentPage() as TokoNowHomeFragment)::startActivityForResult
        )
    }

    private fun setOnScrollListener() {
        productRecom.setScrollListener { scrollState ->
            tokoNowListener?.saveScrollState(adapterPosition, scrollState)
        }
    }

    private fun restoreScrollState() {
        val scrollState = tokoNowListener?.getScrollState(adapterPosition)
        productRecom.restoreScrollState(scrollState)
    }

    fun submitList(data: HomeProductRecomUiModel?) {
        data?.recomWidget?.let {
            productRecom.bind(
                carouselData = RecommendationCarouselData(
                    recommendationData = it,
                    state = RecommendationCarouselData.STATE_READY,
                ),
                widgetListener = this
            )
        }
    }

    interface HomeProductRecomListener {
        fun onRecomProductCardClicked(recomItem: RecommendationItem, channelId: String, headerName: String, position: String, isOoc: Boolean)
        fun onRecomProductCardImpressed(recomItems: List<RecommendationItem>, channelId: String, headerName: String, pageName: String, isOoc: Boolean)
        fun onSeeAllBannerClicked(channelId: String, headerName: String, isOoc: Boolean)
        fun onProductRecomNonVariantClick(recomItem: RecommendationItem, quantity: Int, headerName: String, channelId: String, position: String)
    }
}
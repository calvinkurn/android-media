package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.productcard_compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductRecommendationBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductRecomViewHolder(
    itemView: View,
    private val listener: HomeProductRecomListener? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null
) : AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    ProductCardCompactCarouselView.TokoNowProductCardCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_recommendation
    }

    private var binding: ItemTokopedianowProductRecommendationBinding? by viewBinding()

    private var channelId = ""
    private var headerName = ""

    override fun bind(element: HomeProductRecomUiModel) {
        binding?.apply {
            channelId = element.id
            headerName = element.headerModel?.title.orEmpty()

            renderProductItems(element)
            renderRealTimeRecom(element)

            productRecommendation.setListener(
                productCardCarouselListener = this@HomeProductRecomViewHolder,
                headerCarouselListener = this@HomeProductRecomViewHolder
            )
        }
    }

    override fun bind(element: HomeProductRecomUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            renderProductItems(element)
            renderRealTimeRecom(element)
        }
    }

    private fun renderProductItems(element: HomeProductRecomUiModel) {
        binding?.productRecommendation?.bind(
            items = element.productList,
            seeMoreModel = element.seeMoreModel,
            header = element.headerModel
        )
    }

    private fun renderRealTimeRecom(element: HomeProductRecomUiModel) {
        binding?.realTimeRecommendationCarousel?.apply {
            listener = rtrListener
            analytics = rtrAnalytics
            bind(element.realTimeRecom)
        }
    }

    override fun onSeeAllClicked(
        headerName: String,
        appLink: String
    ) {
        listener?.onSeeAllClicked(
            channelId = channelId,
            appLink = appLink,
            headerName = headerName
        )
    }

    override fun onSeeMoreClicked(
        seeMoreUiModel: ProductCardCompactCarouselSeeMoreUiModel
    ) {
        listener?.onSeeMoreClicked(
            channelId = channelId,
            appLink = seeMoreUiModel.appLink,
            headerName = seeMoreUiModel.headerName
        )
    }

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductRecomClicked(
            product = product,
            channelId = channelId,
            headerName = headerName,
            position = layoutPosition
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductRecomImpressed(
            product = product,
            channelId = channelId,
            headerName = headerName,
            position = layoutPosition
        )
    }

    override fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        listener?.onProductRecomQuantityChanged(
            product = product,
            quantity = quantity,
            channelId = channelId
        )
    }

    override fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductCardAddVariantClicked(
            product = product,
            position = position
        )
    }

    override fun onChannelExpired() { /* nothing to do */ }

    interface HomeProductRecomListener {
        fun onProductRecomClicked(
            product: ProductCardCompactCarouselItemUiModel,
            channelId: String,
            headerName: String,
            position: Int
        )
        fun onProductRecomImpressed(
            product: ProductCardCompactCarouselItemUiModel,
            channelId: String,
            headerName: String,
            position: Int
        )
        fun onSeeAllClicked(
            channelId: String,
            appLink: String,
            headerName: String
        )
        fun onSeeMoreClicked(
            channelId: String,
            appLink: String,
            headerName: String
        )
        fun onProductRecomQuantityChanged(
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int,
            channelId: String
        )
        fun onProductCardAddVariantClicked(
            product: ProductCardCompactCarouselItemUiModel,
            position: Int
        )
    }
}

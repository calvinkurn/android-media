package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.R
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.listener.RealTimeRecommendationListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.customview.ProductCardCompactCarouselView
import com.tokopedia.tokopedianow.common.util.ViewUtil.inflateView
import com.tokopedia.tokopedianow.common.view.RealTimeRecommendationCarouselView
import com.tokopedia.tokopedianow.common.view.TokoNowDynamicHeaderView
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductRecommendationBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.utils.view.binding.viewBinding

class HomeProductRecomViewHolder(
    itemView: View,
    private val listener: HomeProductRecomListener? = null,
    private val rtrListener: RealTimeRecommendationListener? = null,
    private val rtrAnalytics: RealTimeRecommendationAnalytics? = null,
    private val recycledViewPool: RecycledViewPool? = null
) : AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    ProductCardCompactCarouselView.ProductCardCompactCarouselListener,
    TokoNowDynamicHeaderView.TokoNowDynamicHeaderListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_recommendation
    }

    private var binding: ItemTokopedianowProductRecommendationBinding? by viewBinding()
    private var realtimeRecommendationView: RealTimeRecommendationCarouselView? = null

    override fun bind(element: HomeProductRecomUiModel) {
        renderProductItems(element)
        renderRealTimeRecom(element)
        setupListener()
    }

    override fun bind(element: HomeProductRecomUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            renderProductItems(element)
            renderRealTimeRecom(element)
        }
    }

    private fun renderProductItems(element: HomeProductRecomUiModel) {
        binding?.productRecommendation?.apply {
            bind(
                items = element.productList,
                seeMoreModel = element.seeMoreModel,
                header = element.headerModel
            )
            setRecycledViewPool(
                recycledViewPool = recycledViewPool
            )
        }
    }

    private fun renderRealTimeRecom(element: HomeProductRecomUiModel) {
        if(element.realTimeRecom.widgetState != RealTimeRecomWidgetState.IDLE) {
            binding?.apply {
                if(realtimeRecommendationView == null) {
                    val view = realTimeRecommendationViewStub
                        .inflateView(R.layout.layout_tokopedianow_rtr_carousel_view)
                    realtimeRecommendationView = view as? RealTimeRecommendationCarouselView
                }

                realtimeRecommendationView?.apply {
                    listener = rtrListener
                    analytics = rtrAnalytics
                    bind(element.realTimeRecom)
                }
                realtimeRecommendationView?.show()
            }
        } else {
            realtimeRecommendationView?.hide()
        }
    }

    private fun setupListener() {
        binding?.productRecommendation?.setListener(
            productCardCarouselListener = this,
            headerCarouselListener = this
        )
    }

    override fun onSeeAllClicked(
        context: Context,
        channelId: String,
        headerName: String,
        appLink: String,
        widgetId: String
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
            channelId = seeMoreUiModel.id,
            appLink = seeMoreUiModel.appLink,
            headerName = seeMoreUiModel.headerName
        )
    }

    override fun onProductCardAddToCartBlocked() {
        listener?.onProductCardAddToCartBlocked()
    }

    override fun onProductCardClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductRecomClicked(
            product = product,
            channelId = product.channelId,
            headerName = product.headerName,
            position = layoutPosition
        )
    }

    override fun onProductCardImpressed(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        listener?.onProductRecomImpressed(
            product = product,
            channelId = product.channelId,
            headerName = product.headerName,
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
            channelId = product.channelId
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
        fun onProductCardAddToCartBlocked()
    }
}

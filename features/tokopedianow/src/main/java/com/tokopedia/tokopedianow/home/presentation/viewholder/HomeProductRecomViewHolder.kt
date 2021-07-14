package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.presentation.fragment.TokoNowHomeFragment
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

class HomeProductRecomViewHolder (
    itemView: View,
    private val tokoNowListener: TokoNowView? = null,
    private val listener: HomeProductRecomListener? = null
): AbstractViewHolder<HomeProductRecomUiModel>(itemView),
    CarouselProductCardListener.OnItemClickListener,
    CarouselProductCardListener.OnATCNonVariantClickListener,
    CarouselProductCardListener.OnAddVariantClickListener
{

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_product_recom
    }

    private val productCardWidget: CarouselProductCardView by lazy { itemView.findViewById(R.id.product_card) }

    private var productCards: List<HomeProductCardUiModel> = listOf()

    override fun bind(element: HomeProductRecomUiModel) {
        productCards = element.productCards
        productCardWidget.bindCarouselProductCardViewGrid(
            mapToProductCard(element),
            carouselProductCardOnItemClickListener = this,
            carouselProductCardOnItemATCNonVariantClickListener = this,
            carouselProductCardOnItemAddVariantClickListener = this,
            recyclerViewPool = listener?.getCarouselRecycledViewPool()
        )
    }

    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
        RouteManager.route(
            itemView.context,
            ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
            productCards[carouselProductCardPosition].id
        )
    }

    override fun onATCNonVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int, quantity: Int) {
        listener?.onProductRecomNonVariantClick(productCards[carouselProductCardPosition], quantity)
    }

    override fun onAddVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
        val productCard = productCards[carouselProductCardPosition]
        AtcVariantHelper.goToAtcVariant(
            context = itemView.context,
            productId = productCards[carouselProductCardPosition].id,
            pageSource = "tokonow",
            isTokoNow = true,
            shopId = productCard.shopId,
            startActivitResult = (tokoNowListener?.getFragmentPage() as TokoNowHomeFragment)::startActivityForResult
        )
    }

    private fun mapToProductCard(element: HomeProductRecomUiModel): List<ProductCardModel> {
        val productCards = mutableListOf<ProductCardModel>()
        element.productCards.map {
            productCards.add(it.productCardModel)
        }
        return productCards
    }

    interface HomeProductRecomListener {
        fun getCarouselRecycledViewPool(): RecyclerView.RecycledViewPool
        fun onProductRecomNonVariantClick(productCard: HomeProductCardUiModel, quantity: Int)
    }
}
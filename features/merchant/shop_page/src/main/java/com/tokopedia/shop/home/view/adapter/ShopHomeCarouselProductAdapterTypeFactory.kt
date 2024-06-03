package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductItemBigGridViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductItemListScrollableViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductItemListViewHolder
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopHomeCarouselProductAdapterTypeFactory(
    private val shopHomeCarouselProductUiModel: ShopHomeCarousellProductUiModel,
    private val listProductCardModel: List<ProductCardModel>,
    private val carouselProductCardOnItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener?,
    private val carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener?,
    private val carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener?,
    private val carouselProductCardOnItemATCNonVariantClickListener: CarouselProductCardListener.OnATCNonVariantClickListener?,
    private val carouselProductCardOnItemAddVariantClickListener: CarouselProductCardListener.OnAddVariantClickListener?,
    private val isOverrideWidgetTheme: Boolean,
    private val productCardType: ProductCardType
) : BaseAdapterTypeFactory(), TypeFactoryShopHomeProductCarousel {

    companion object {
        private const val TWO_PRODUCT = 2
    }
    enum class ProductCardType  {
        LIST,
        GRID
    }

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when{
            shopHomeCarouselProductUiModel.productList.size == Int.ONE -> {
                ShopHomeCarouselProductItemListViewHolder.LAYOUT
            }
            shopHomeCarouselProductUiModel.productList.size == TWO_PRODUCT -> {
                ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT
            }
            productCardType == ProductCardType.GRID -> ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT
            productCardType == ProductCardType.LIST -> ShopHomeCarouselProductItemListScrollableViewHolder.LAYOUT
            else -> ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeCarouselProductItemListViewHolder.LAYOUT -> {
                ShopHomeCarouselProductItemListViewHolder(
                    itemView = parent,
                    listProductCardModel = listProductCardModel,
                    carouselProductCardOnItemAddToCartListener = carouselProductCardOnItemAddToCartListener,
                    carouselProductCardOnItemClickListener = carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener = carouselProductCardOnItemImpressedListener,
                    carouselProductCardOnItemATCNonVariantClickListener = carouselProductCardOnItemATCNonVariantClickListener,
                    carouselProductCardOnItemAddVariantClickListener = carouselProductCardOnItemAddVariantClickListener,
                    isOverrideWidgetTheme = isOverrideWidgetTheme
                )
            }
            ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT -> {
                ShopHomeCarouselProductItemBigGridViewHolder(
                    itemView = parent,
                    listProductCardModel = listProductCardModel,
                    carouselProductCardOnItemAddToCartListener = carouselProductCardOnItemAddToCartListener,
                    carouselProductCardOnItemClickListener = carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener = carouselProductCardOnItemImpressedListener,
                    carouselProductCardOnItemATCNonVariantClickListener = carouselProductCardOnItemATCNonVariantClickListener,
                    carouselProductCardOnItemAddVariantClickListener = carouselProductCardOnItemAddVariantClickListener,
                    isOverrideWidgetTheme = isOverrideWidgetTheme
                )
            }
            ShopHomeCarouselProductItemListScrollableViewHolder.LAYOUT -> {
                ShopHomeCarouselProductItemListScrollableViewHolder(
                    itemView = parent,
                    listProductCardModel = listProductCardModel,
                    carouselProductCardOnItemAddToCartListener = carouselProductCardOnItemAddToCartListener,
                    carouselProductCardOnItemClickListener = carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener = carouselProductCardOnItemImpressedListener,
                    carouselProductCardOnItemATCNonVariantClickListener = carouselProductCardOnItemATCNonVariantClickListener,
                    carouselProductCardOnItemAddVariantClickListener = carouselProductCardOnItemAddVariantClickListener,
                    isOverrideWidgetTheme = isOverrideWidgetTheme
                )
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}

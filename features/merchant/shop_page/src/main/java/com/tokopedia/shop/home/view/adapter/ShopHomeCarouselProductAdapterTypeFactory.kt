package com.tokopedia.shop.home.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeCarouselProductItemBigGridViewHolder
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
) : BaseAdapterTypeFactory(), TypeFactoryShopHomeProductCarousel {

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when(shopHomeCarouselProductUiModel.productList.size){
            Int.ONE -> {
                ShopHomeCarouselProductItemListViewHolder.LAYOUT
            }
            else -> {
                ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT
            }
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopHomeCarouselProductItemListViewHolder.LAYOUT -> {
                ShopHomeCarouselProductItemListViewHolder(
                    parent,
                    listProductCardModel,
                    carouselProductCardOnItemAddToCartListener,
                    carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener,
                    carouselProductCardOnItemATCNonVariantClickListener,
                    carouselProductCardOnItemAddVariantClickListener
                )
            }
            ShopHomeCarouselProductItemBigGridViewHolder.LAYOUT -> {
                ShopHomeCarouselProductItemBigGridViewHolder(
                    parent,
                    listProductCardModel,
                    carouselProductCardOnItemAddToCartListener,
                    carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener,
                    carouselProductCardOnItemATCNonVariantClickListener,
                    carouselProductCardOnItemAddVariantClickListener,
                )
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}

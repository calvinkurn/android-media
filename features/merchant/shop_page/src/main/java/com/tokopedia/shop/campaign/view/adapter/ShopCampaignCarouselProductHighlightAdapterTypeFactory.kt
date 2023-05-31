package com.tokopedia.shop.campaign.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignCarouselProductItemBigGridViewHolder
import com.tokopedia.shop.campaign.view.adapter.viewholder.ShopCampaignCarouselProductItemListViewHolder
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.home.view.adapter.TypeFactoryShopHomeProductCarousel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel

class ShopCampaignCarouselProductHighlightAdapterTypeFactory(
    private val shopHomeCarouselProductUiModel: ShopCampaignWidgetCarouselProductUiModel,
    private val listProductCardModel: List<ProductCardModel>,
    private val carouselProductCardOnItemClickListener: CarouselProductCardListener.OnItemClickListener?,
    private val carouselProductCardOnItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener?
) : BaseAdapterTypeFactory(), TypeFactoryShopHomeProductCarousel {

    override fun type(shopHomeProductViewModel: ShopHomeProductUiModel): Int {
        return when(shopHomeCarouselProductUiModel.productList.size){
            Int.ONE -> {
                ShopCampaignCarouselProductItemListViewHolder.LAYOUT
            }
            else -> {
                ShopCampaignCarouselProductItemBigGridViewHolder.LAYOUT
            }
        }
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ShopCampaignCarouselProductItemListViewHolder.LAYOUT -> {
                ShopCampaignCarouselProductItemListViewHolder(
                    parent,
                    listProductCardModel,
                    carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener,
                )
            }
            ShopCampaignCarouselProductItemBigGridViewHolder.LAYOUT -> {
                ShopCampaignCarouselProductItemBigGridViewHolder(
                    parent,
                    listProductCardModel,
                    carouselProductCardOnItemClickListener,
                    carouselProductCardOnItemImpressedListener,
                )
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}

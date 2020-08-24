package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeCampaignCarouselProductItemViewHolder(
        itemView: View,
        private val parentPosition: Int,
        private val shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeProductViewModel>(itemView) {
    lateinit var productCard: ProductCardGridView
    protected var shopHomeProductViewModel: ShopHomeProductViewModel? = null

    init {
        findViews(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_carousel_product_card
    }

    private fun findViews(view: View) {
        productCard = view.findViewById(R.id.product_card)
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductViewModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
        productCard.applyCarousel()
        productCard.setProductModel(ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = shopHomeProductViewModel
        ))
        setListener()
    }

    protected open fun setListener() {
        val totalProduct = shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.productList?.size?.minus(1) ?: 0
        productCard.setOnClickListener {
            shopHomeCampaignNplWidgetListener.onCampaignCarouselProductItemClicked(
                    parentPosition,
                    totalProduct - adapterPosition,
                    shopHomeNewProductLaunchCampaignUiModel,
                    shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let {
            productCard.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    shopHomeCampaignNplWidgetListener.onCampaignCarouselProductItemImpression(
                            parentPosition,
                            totalProduct - adapterPosition,
                            shopHomeNewProductLaunchCampaignUiModel,
                            shopHomeProductViewModel
                    )
                }
            })
        }
    }
}
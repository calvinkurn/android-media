package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopCarouselProductCardBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeCampaignCarouselProductItemViewHolder(
    itemView: View,
    private val parentPosition: Int,
    private val shopHomeNewProductLaunchCampaignUiModel: ShopHomeNewProductLaunchCampaignUiModel,
    private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {
    private val viewBinding: ItemShopCarouselProductCardBinding? by viewBinding()
    private var productCard: ProductCardGridView? = null
    protected var shopHomeProductViewModel: ShopHomeProductUiModel? = null

    init {
        findViews(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_carousel_product_card
    }

    private fun findViews(view: View) {
        productCard = viewBinding?.productCard
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductUiModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
        productCard?.applyCarousel()
        productCard?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = shopHomeProductViewModel,
                widgetName = shopHomeNewProductLaunchCampaignUiModel.name,
                statusCampaign = shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.statusCampaign.orEmpty()
            )
        )
        setListener()
    }

    protected open fun setListener() {
        productCard?.setOnClickListener {
            shopHomeCampaignNplWidgetListener.onCampaignCarouselProductItemClicked(
                parentPosition,
                adapterPosition,
                shopHomeNewProductLaunchCampaignUiModel,
                shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let {
            productCard?.setImageProductViewHintListener(
                it,
                object : ViewHintListener {
                    override fun onViewHint() {
                        shopHomeCampaignNplWidgetListener.onCampaignCarouselProductItemImpression(
                            parentPosition,
                            adapterPosition,
                            shopHomeNewProductLaunchCampaignUiModel,
                            shopHomeProductViewModel
                        )
                    }
                }
            )
        }
    }
}

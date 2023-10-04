package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
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
        private const val RED_STOCK_BAR_LABEL_MATCH_VALUE = "segera habis"
        // currently set to 1, since there's a clickable area on which is a part of the carousel
        private const val NON_PRODUCT_CARD_ITEM = 1
    }

    private fun findViews(view: View) {
        productCard = viewBinding?.productCard
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductUiModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
        productCard?.applyCarousel()
        val stockBarLabel = shopHomeProductViewModel.stockLabel
        var stockBarLabelColor = ""
        if (stockBarLabel.equals(RED_STOCK_BAR_LABEL_MATCH_VALUE, ignoreCase = true)) {
            stockBarLabelColor = ShopUtil.getColorHexString(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_RN600
            )
        }
        productCard?.setProductModel(
            ShopPageHomeMapper.mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                shopHomeProductViewModel = shopHomeProductViewModel,
                widgetName = shopHomeNewProductLaunchCampaignUiModel.name,
                statusCampaign = shopHomeNewProductLaunchCampaignUiModel.data?.firstOrNull()?.statusCampaign.orEmpty(),
                forceLightModeColor = shopHomeCampaignNplWidgetListener.isForceLightModeColorOnCampaignNplWidget()
            ).copy(
                stockBarLabelColor = stockBarLabelColor
            )
        )
        setListener()
    }

    protected open fun setListener() {
        productCard?.setOnClickListener {
            shopHomeCampaignNplWidgetListener.onCampaignCarouselProductItemClicked(
                parentPosition,
                ShopUtil.getActualPositionFromIndex(adapterPosition) - NON_PRODUCT_CARD_ITEM,
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
                            ShopUtil.getActualPositionFromIndex(adapterPosition) - NON_PRODUCT_CARD_ITEM,
                            shopHomeNewProductLaunchCampaignUiModel,
                            shopHomeProductViewModel
                        )
                    }
                }
            )
        }
    }
}

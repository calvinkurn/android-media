package com.tokopedia.home_component.widget.special_release

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseRevampItemBinding
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by frenzel
 */
class SpecialReleaseRevampItemViewHolder(itemView: View) : AbstractViewHolder<SpecialReleaseRevampItemDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_special_release_revamp_item
        private const val className = "com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampItemViewHolder"
    }

    private val binding: HomeComponentSpecialReleaseRevampItemBinding? by viewBinding()

    override fun bind(element: SpecialReleaseRevampItemDataModel) {
        binding?.run {
            cardContainer.cardType = CardUnify2.TYPE_BORDER
            cardContainer.animateOnPress = element.cardInteraction
            renderShop(element)
            renderProduct(element)
        }
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.renderShop(element: SpecialReleaseRevampItemDataModel) {
        val grid = element.grid
        val shop = grid.shop
        setShopListener(element)
        renderShopImage(grid)
        renderShopTitleAndSubtitle(grid)
        renderShopCta(shop)
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.setShopListener(element: SpecialReleaseRevampItemDataModel) {
        val shopClickListener = View.OnClickListener {
            element.listener.onShopClicked(
                element.trackingAttributionModel,
                element.grid,
                element.grid.shop.shopApplink
            )
        }
        cardContainer.setOnClickListener(shopClickListener)
        cta.setOnClickListener(shopClickListener)
        cardContainer.addOnImpressionListener(element.shopImpressHolder) {
            element.listener.onShopImpressed(
                element.trackingAttributionModel,
                element.grid
            )
        }
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.renderShopImage(grid: ChannelGrid) {
        shopImage.loadImage(grid.shop.shopProfileUrl)
        if(grid.badges.isEmpty()) {
            containerShopBadge.gone()
        } else {
            containerShopBadge.visible()
            shopBadge.loadImage(grid.badges[0].imageUrl)
        }
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.renderShopTitleAndSubtitle(grid: ChannelGrid) {
        shopName.text = grid.shop.shopName
        if(grid.benefit.value.isNotEmpty()) {
            groupLocation.gone()
            label.visible()
            label.text = grid.benefit.value
        } else {
            label.gone()
            groupLocation.visible()
            textLocation.text = grid.shop.shopLocation
        }
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.renderShopCta(shop: ChannelShop) {
        if(shop.shopApplink.isEmpty()) cta.gone() else cta.visible()
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.renderProduct(element: SpecialReleaseRevampItemDataModel) {
        setProductListener(element)
        productCard.setProductModel(element.productCardModel)
    }

    private fun HomeComponentSpecialReleaseRevampItemBinding.setProductListener(element: SpecialReleaseRevampItemDataModel) {
        val productClickListener = View.OnClickListener {
            if(element.grid.isTopads){
                TopAdsUrlHitter(itemView.context).hitClickUrl(
                    className,
                    element.grid.impression,
                    element.grid.id,
                    element.grid.name,
                    element.grid.imageUrl
                )
            }
            element.listener.onProductCardClicked(
                element.trackingAttributionModel,
                element.grid,
                element.grid.position,
                element.grid.applink
            )
        }
        productLayout.setOnClickListener(productClickListener)
        productCard.run {
            setOnClickListener(productClickListener)
            addOnImpressionListener(element.productImpressHolder) {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(context).hitImpressionUrl(
                        className,
                        element.grid.impression,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl
                    )
                }
                element.listener.onProductCardImpressed(
                    element.trackingAttributionModel,
                    element.grid,
                    element.grid.position,
                )
            }
        }
    }
}

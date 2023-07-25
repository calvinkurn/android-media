package com.tokopedia.home_component.widget.special_release

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentSpecialReleaseRevampItemBinding
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.home_component.util.loadImage
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.ProductCardModel
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
        renderShop(element)
        renderProduct(element)
    }

    private fun renderShop(element: SpecialReleaseRevampItemDataModel) {
        binding?.run {
            val grid = element.grid
            val shop = grid.shop
            shopImage.loadImage(shop.shopProfileUrl)
            if(grid.badges.isEmpty()) {
                containerShopBadge.gone()
            } else {
                containerShopBadge.visible()
                shopBadge.loadImage(grid.badges[0].imageUrl)
            }
            shopName.text = shop.shopName
            if(grid.benefit.value.isNotEmpty()) {
                groupLocation.gone()
                label.visible()
                label.text = grid.label
            } else {
                label.gone()
                groupLocation.visible()
                textLocation.text = grid.shop.shopLocation
            }
            container.setOnClickListener {
                element.listener.onShopClicked(element.channel, grid, grid.shop.shopApplink)
            }
            container.addOnImpressionListener(element.shopImpressHolder) {
                element.listener.onShopImpressed(
                    element.channel,
                    element.grid
                )
            }
        }
    }

    private fun renderProduct(element: SpecialReleaseRevampItemDataModel) {
        binding?.productCard?.run {
            val productCardModel = ChannelModelMapper.mapToProductCardModel(
                channelGrid = element.grid,
                animateOnPress = CardUnify2.ANIMATE_NONE,
                cardType = CardUnify2.TYPE_CLEAR,
                productCardListType = ProductCardModel.ProductListType.BEST_SELLER
            )
            setProductModel(productCardModel)
            setOnClickListener {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(context).hitClickUrl(
                        className,
                        element.grid.impression,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl
                    )
                }
                element.listener.onProductCardClicked(
                    element.channel,
                    element.grid,
                    element.grid.position,
                    element.grid.applink
                )
            }
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
                    element.channel,
                    element.grid,
                    element.grid.position,
                )
            }
        }
    }
}

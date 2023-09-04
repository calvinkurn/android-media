package com.tokopedia.home_component.widget.shop_flash_sale.tab

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.databinding.HomeComponentShopFlashSaleTabItemBinding
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.util.getHexColorFromIdColor
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ShopTabViewHolder(
    itemView: View,
    private val shopTabListener: ShopTabListener? = null,
): RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.home_component_shop_flash_sale_tab_item
    }

    private val binding: HomeComponentShopFlashSaleTabItemBinding? by viewBinding()

    fun bind(tab: ShopTabDataModel) {
        setTabListener(tab)
        bindIndicator(tab)
        bindShop(tab)
    }

    fun bindIndicator(tab: ShopTabDataModel) {
        setTabBackground(tab.isActivated)
        setTabIndicator(tab.isActivated)
    }

    fun bindShop(tab: ShopTabDataModel) {
        renderShopImage(tab)
        renderShopName(tab)
    }

    private fun setTabListener(tab: ShopTabDataModel) {
        binding?.containerShopFlashSaleTab?.setOnClickListener {
            shopTabListener?.onShopTabClick(tab)
        }
    }

    private fun setTabBackground(isActivated: Boolean) {
        if(isActivated) {
            binding?.containerShopFlashSaleTab?.setGradientBackground(
                arrayListOf(
                    getHexColorFromIdColor(itemView.context, unifyprinciplesR.color.Unify_NN0),
                    getHexColorFromIdColor(itemView.context, unifyprinciplesR.color.Unify_GN50)
                )
            )
        } else {
            binding?.containerShopFlashSaleTab?.setBackgroundColor(
                ContextCompat.getColor(itemView.context, android.R.color.transparent)
            )
        }
    }

    private fun setTabIndicator(isActivated: Boolean) {
        binding?.imgShopFlashSaleTabIndicator?.apply {
            if(isActivated) show() else hide()
        }
    }

    private fun renderShopImage(tab: ShopTabDataModel) {
        binding?.imgShopFlashSaleTabShopImage?.loadImage(tab.imageUrl)
        if(tab.badgesUrl.isEmpty()) {
            binding?.containerShopFlashSaleTabShopBadge?.gone()
        } else {
            binding?.containerShopFlashSaleTabShopBadge?.visible()
            binding?.imgShopFlashSaleTabShopBadge?.loadImage(tab.badgesUrl)
        }
    }

    private fun renderShopName(tab: ShopTabDataModel) {
        binding?.txtShopFlashSaleTabName?.text = tab.shopName
    }
}

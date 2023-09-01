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

internal class ShopFlashSaleTabViewHolder(
    itemView: View,
    private val shopTabListener: ShopTabListener? = null,
): RecyclerView.ViewHolder(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = com.tokopedia.home_component.R.layout.home_component_shop_flash_sale_tab_item
    }

    private val binding: HomeComponentShopFlashSaleTabItemBinding? by viewBinding()

    fun bind(tab: ShopFlashSaleTabDataModel) {
        setTabListener(tab)
        bindIndicator(tab)
        bindShop(tab)
    }

    fun bindIndicator(tab: ShopFlashSaleTabDataModel) {
        setTabBackground(tab.isActivated)
        setTabIndicator(tab.isActivated)
    }

    fun bindShop(tab: ShopFlashSaleTabDataModel) {
        renderShopImage(tab.channelGrid)
        renderShopName(tab.channelGrid)
    }

    private fun setTabListener(tab: ShopFlashSaleTabDataModel) {
        binding?.containerShopFlashSaleTab?.setOnClickListener {
            shopTabListener?.onShopTabClick(tab)
        }
    }

    private fun setTabBackground(isActivated: Boolean) {
        if(isActivated) {
            binding?.containerShopFlashSaleTab?.setGradientBackground(
                arrayListOf(
                    getHexColorFromIdColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0),
                    getHexColorFromIdColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
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

    private fun renderShopImage(grid: ChannelGrid) {
        binding?.imgShopFlashSaleTabShopImage?.loadImage(grid.imageUrl)
        if(grid.badges.isEmpty()) {
            binding?.containerShopFlashSaleTabShopBadge?.gone()
        } else {
            binding?.containerShopFlashSaleTabShopBadge?.visible()
            binding?.imgShopFlashSaleTabShopBadge?.loadImage(grid.badges[0].imageUrl)
        }
    }

    private fun renderShopName(grid: ChannelGrid) {
        binding?.txtShopFlashSaleTabName?.text = grid.name
    }
}

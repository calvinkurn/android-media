package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeV4TerlarisUiModel

class ShopHomeV4TerlarisViewHolder(
    itemView: View,
    private val listener: ShopHomeV4TerlarisViewHolderListener
): AbstractViewHolder<ShopHomeV4TerlarisUiModel>(itemView) {

    interface ShopHomeV4TerlarisViewHolderListener {
        fun onProductClick(productId: String)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_v4_terlaris_widget
    }

    override fun bind(element: ShopHomeV4TerlarisUiModel?) {
        TODO("Not yet implemented")
    }

    private fun redirectToPdp(productId: String) {
        listener.onProductClick(productId)
    }

}

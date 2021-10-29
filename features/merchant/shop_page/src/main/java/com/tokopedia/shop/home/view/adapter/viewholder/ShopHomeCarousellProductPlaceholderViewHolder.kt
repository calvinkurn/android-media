package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopCarouselProductPlaceholderAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel

class ShopHomeCarousellProductPlaceholderViewHolder(
        itemView: View,
) : AbstractViewHolder<ShopHomeCarousellProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel_placeholder
    }

    private val recyclerView: RecyclerView? = itemView.findViewById(R.id.rv_product_carousel_placeholder)

    override fun bind(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel) {
        recyclerView?.apply {
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
            adapter = ShopCarouselProductPlaceholderAdapter()
            adapter?.notifyDataSetChanged()
        }
    }

}

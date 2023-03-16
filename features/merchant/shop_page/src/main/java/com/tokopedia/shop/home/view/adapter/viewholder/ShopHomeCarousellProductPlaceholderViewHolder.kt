package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeProductCarouselPlaceholderBinding
import com.tokopedia.shop.home.view.adapter.ShopCarouselProductPlaceholderAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeCarousellProductPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<BaseShopHomeWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_carousel_placeholder
    }
    private val viewBinding: ItemShopHomeProductCarouselPlaceholderBinding? by viewBinding()
    private val recyclerView: RecyclerView? = viewBinding?.rvProductCarouselPlaceholder

    override fun bind(baseShopHomeWidgetUiModel: BaseShopHomeWidgetUiModel) {
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

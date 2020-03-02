package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductViewHolder

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory) {

    companion object{
        private const val ALL_PRODUCT_STRING = "Semua Produk"
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = getItemViewType(position) != ShopProductViewHolder.GRID_LAYOUT
        }
        super.onBindViewHolder(holder, position)
    }

    fun setProductListData(productList: List<ShopHomeProductViewModel>) {
        val lastIndex = lastIndex
        visitables.addAll(productList)
        notifyItemRangeInserted(lastIndex, productList.size)
    }

    fun setEtalaseTitleData() {
        visitables.add(ShopHomeProductEtalaseTitleUiModel(ALL_PRODUCT_STRING, ""))
        notifyItemInserted(lastIndex)
    }

    fun setHomeLayoutData(data: List<BaseShopHomeWidgetUiModel>) {
        val lastIndex = lastIndex
        visitables.addAll(data)
        notifyItemRangeInserted(lastIndex, data.size)
    }
}
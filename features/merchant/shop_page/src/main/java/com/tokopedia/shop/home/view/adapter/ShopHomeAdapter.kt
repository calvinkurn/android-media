package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
): BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener{

    companion object{
        private const val ALL_PRODUCT_STRING = "Semua Produk"
    }

    private var productListViewModel: MutableList<ShopHomeProductViewModel> = mutableListOf()

    override fun onBindViewHolder(holder: AbstractViewHolder<*>, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            layoutParams.isFullSpan = getItemViewType(position) != ShopHomeProductViewHolder.LAYOUT
        }
        super.onBindViewHolder(holder, position)
    }

    fun setProductListData(productList: List<ShopHomeProductViewModel>) {
        val lastIndex = visitables.size
        productListViewModel.addAll(productList)
        visitables.addAll(productList)
        notifyItemRangeInserted(lastIndex, productList.size)
    }

    fun setEtalaseTitleData() {
        visitables.add(ShopHomeProductEtalaseTitleUiModel(ALL_PRODUCT_STRING, ""))
        notifyItemInserted(visitables.size)
    }

    fun setHomeLayoutData(data: List<BaseShopHomeWidgetUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(data)
        notifyItemRangeInserted(lastIndex, data.size)
    }

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            notifyItemRemoved(itemPosition)
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            notifyItemRemoved(itemPosition)
        }
    }

    override fun getEndlessDataSize(): Int {
        return productListViewModel.size
    }

}
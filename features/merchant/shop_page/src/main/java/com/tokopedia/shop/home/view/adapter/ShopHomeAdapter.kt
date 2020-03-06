package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.newproduct.view.viewholder.ShopProductViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder

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
            layoutParams.isFullSpan = getItemViewType(position) != ShopHomeProductViewHolder.LAYOUT
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getLastIndex() = visitables.size

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

//    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
//        super.onViewAttachedToWindow(holder)
//
//        val video = element.takeIf { it.name == WidgetYoutubeVideo }
//        when(holder) {
//            is ShopHomeVideoViewHolder -> {
//                if (context != null) {
//                    video?.binder?.bind(context, holder, fragmentManager)
//                }
//            }
//        }
//    }

//    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
//        super.onViewDetachedFromWindow(holder)
//        val video = element.takeIf { it.name == WidgetYoutubeVideo }
//        when(holder) {
//            is ShopHomeVideoViewHolder -> {
//                if (context != null) {
//                    video?.binder?.unBind(holder, fragmentManager)
//                }
//            }
//        }
//    }

}
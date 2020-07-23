package com.tokopedia.shop.home.view.adapter

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomePlayCarouselViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory), DataEndlessScrollListener.OnDataEndlessScrollListener {

    companion object {
        private const val ALL_PRODUCT_STRING = "Semua Produk"

        const val ON_PAUSE = "on_pause"
        const val ON_RESUME = "on_resume"
        const val ON_DESTROY = "on_resume"
    }

    var isOwner: Boolean = false
    private var productListViewModel: MutableList<ShopHomeProductViewModel> = mutableListOf()

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if(holder is ShopHomePlayCarouselViewHolder) {
            holder.onResume()
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        if(holder is ShopHomePlayCarouselViewHolder) {
            holder.onPause()
        }
        super.onViewDetachedFromWindow(holder)
    }

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

    fun updatePlayWidget(playCarouselUiModel: ShopHomePlayCarouselUiModel){
        visitables.withIndex().find { (_, data) -> data is ShopHomePlayCarouselUiModel }?.let {
            if(playCarouselUiModel.playBannerCarouselDataModel.channelList.isEmpty()){
                visitables.removeAt(it.index)
                notifyItemRemoved(it.index)
            } else {
                visitables[it.index] = playCarouselUiModel
                notifyItemChanged(it.index)
            }
        }
    }

    fun setEtalaseTitleData() {
        visitables.add(ShopHomeProductEtalaseTitleUiModel(ALL_PRODUCT_STRING, ""))
        notifyItemInserted(visitables.size)
    }

    fun setHomeLayoutData(data: List<BaseShopHomeWidgetUiModel>) {
        visitables.clear()
        visitables.addAll(data)
        notifyDataSetChanged()
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

    fun getAllProductWidgetPosition(): Int {
        return visitables.filter {
            (it !is LoadingModel) && (it !is LoadingMoreModel) && (it !is ShopHomeProductEtalaseTitleUiModel)
        }.indexOfFirst { it is ShopHomeProductViewModel }
    }

    fun updateProductWidgetData(shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel) {
        val position = visitables.indexOf(shopHomeCarousellProductUiModel)
        notifyItemChanged(position)
    }

    fun updateWishlistProduct(productId: String, isWishlist: Boolean) {
        visitables.filterIsInstance<ShopHomeProductViewModel>().onEach {
            if(it.id == productId){
                it.isWishList = isWishlist
                notifyItemChanged(visitables.indexOf(it))
            }
        }
        visitables.filterIsInstance<ShopHomeCarousellProductUiModel>().onEach { shopHomeCarousellProductUiModel ->
            val totalFoundProductId = shopHomeCarousellProductUiModel.productList.filter {
                it.id == productId
            }.onEach {
                it.isWishList = isWishlist
            }.size
            if(totalFoundProductId != 0)
                notifyItemChanged(visitables.indexOf(shopHomeCarousellProductUiModel))
        }
    }

    fun pausePlayCarousel(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ON_PAUSE, true)
        })
    }

    fun resumePlayCarousel(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ON_RESUME, true)
        })
    }

    fun onDestroy(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ON_DESTROY, true)
        })
    }

    private fun getPositionPlayCarousel(): Int{
        return visitables.indexOfFirst { it is ShopHomePlayCarouselUiModel}
    }

}
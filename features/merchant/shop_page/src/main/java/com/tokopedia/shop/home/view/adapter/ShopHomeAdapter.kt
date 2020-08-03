package com.tokopedia.shop.home.view.adapter

import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
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
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        private val shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory),
        DataEndlessScrollListener.OnDataEndlessScrollListener,
        StickySingleHeaderView.OnStickySingleHeaderAdapter {

    companion object {
        private const val ALL_PRODUCT_STRING = "Semua Produk"
    }

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    var isOwner: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var productListViewModel: MutableList<ShopHomeProductViewModel> = mutableListOf()
    val shopHomeEtalaseTitlePosition: Int
        get() = visitables.indexOfFirst {
            it.javaClass == ShopHomeProductEtalaseTitleUiModel::class.java
        }.takeIf { it != -1 } ?: 0
    private val shopProductEtalaseListPosition: Int
        get() = visitables.indexOfFirst {
            it.javaClass == ShopProductSortFilterUiModel::class.java
        }.takeIf { it != -1 } ?: 0

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
            layoutParams.isFullSpan = !(getItemViewType(position) == ShopHomeProductViewHolder.LAYOUT ||
                    getItemViewType(position) == LoadingMoreViewHolder.LAYOUT)
        }
        super.onBindViewHolder(holder, position)
    }

    override fun clearAllElements() {
        super.clearAllElements()
        refreshSticky()
    }

    fun setProductListData(productList: List<ShopHomeProductViewModel>, initialData: Boolean) {
        val lastIndex = visitables.size
        productListViewModel.addAll(productList)
        visitables.addAll(productList)
        if (initialData)
            notifyChangedDataSet()
        else
            notifyInsertedItemRange(lastIndex, productList.size)
    }

    fun updatePlayWidget(playCarouselUiModel: ShopHomePlayCarouselUiModel){
        visitables.indexOfFirst { it is ShopHomePlayCarouselUiModel }.let { index ->
            if(playCarouselUiModel.playBannerCarouselDataModel.channelList.isEmpty()){
                visitables.removeAt(index)
                notifyItemRemoved(index)
            } else {
                visitables[index] = playCarouselUiModel
                notifyItemChanged(index)
            }
        }
    }

    fun setEtalaseTitleData() {
        val etalaseTitleUiModel = ShopHomeProductEtalaseTitleUiModel(ALL_PRODUCT_STRING, "")
        visitables.add(etalaseTitleUiModel)
    }

    fun setSortFilterData(shopProductSortFilterUiModel: ShopProductSortFilterUiModel) {
        visitables.add(shopProductSortFilterUiModel)
    }

    fun setHomeLayoutData(data: List<BaseShopHomeWidgetUiModel>) {
        visitables.clear()
        visitables.addAll(data)
        notifyChangedDataSet()
    }

    fun setHomeYouTubeData(widgetId: String, data: YoutubeVideoDetailModel) {
        visitables.filterIsInstance<ShopHomeDisplayWidgetUiModel>()
                .find {
                    it.widgetId == widgetId
                }?.let {
                    it.data?.firstOrNull()?.youTubeVideoDetail = data
                    notifyChangedItem(visitables.indexOf(it))
                }
    }

    override fun hideLoading() {
        if (visitables.contains(loadingModel)) {
            val itemPosition = visitables.indexOf(loadingModel)
            visitables.remove(loadingModel)
            notifyRemovedItem(itemPosition)
        } else if (visitables.contains(loadingMoreModel)) {
            val itemPosition = visitables.indexOf(loadingMoreModel)
            visitables.remove(loadingMoreModel)
            notifyRemovedItem(itemPosition)
        }
    }

    override fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) {
                visitables.add(loadingMoreModel)
            } else {
                visitables.add(loadingModel)
            }
            notifyInsertedItem(visitables.size - 1)
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
        notifyChangedItem(position)
    }

    fun updateWishlistProduct(productId: String, isWishlist: Boolean) {
        visitables.filterIsInstance<ShopHomeProductViewModel>().onEach {
            if (it.id == productId) {
                it.isWishList = isWishlist
                notifyChangedItem(visitables.indexOf(it))
            }
        }
        visitables.filterIsInstance<ShopHomeCarousellProductUiModel>().onEach { shopHomeCarousellProductUiModel ->
            val totalFoundProductId = shopHomeCarousellProductUiModel.productList.filter {
                it.id == productId
            }.onEach {
                it.isWishList = isWishlist
            }.size
            if (totalFoundProductId != 0)
                notifyChangedItem(visitables.indexOf(shopHomeCarousellProductUiModel))
        }
    }

    override fun onStickyHide() {
        notifyChangedItem(shopProductEtalaseListPosition)
    }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return shopHomeAdapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun getStickyHeaderPosition(): Int {
        return visitables.indexOfFirst {
            it::class.java == ShopProductSortFilterUiModel::class.java
        }
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is ShopProductSortFilterViewHolder) {
            visitables.filterIsInstance(ShopProductSortFilterUiModel::class.java).firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun isShowLoadingMore(): Boolean {
        return visitables.filterIsInstance<ShopHomeProductViewModel>().isNotEmpty()
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    fun changeSelectedSortFilter(sortId: String, sortName: String) {
        val shopProductSortFilterUiViewModel = visitables
                .filterIsInstance<ShopProductSortFilterUiModel>().firstOrNull()
        shopProductSortFilterUiViewModel?.apply {
            selectedSortId = sortId
            selectedSortName = sortName
        }
        notifyChangedItem(visitables.indexOf(shopProductSortFilterUiViewModel))
    }

    fun removeProductList() {
        val firstProductViewModelIndex = visitables.indexOfFirst {
            it::class.java == ShopHomeProductViewModel::class.java
        }
        val totalProductViewModelData = visitables.filterIsInstance<ShopHomeProductViewModel>().size
        if (firstProductViewModelIndex >= 0 && totalProductViewModelData <= visitables.size && firstProductViewModelIndex < totalProductViewModelData) {
            visitables.removeAll(visitables.filterIsInstance<ShopHomeProductViewModel>())
            productListViewModel.clear()
            notifyRemovedItemRange(firstProductViewModelIndex, totalProductViewModelData)
        }
    }

    private fun notifyChangedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemChanged(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyRemovedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemRemoved(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyRemovedItemRange(startPosition: Int, totalItem: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, startPosition)) {
                notifyItemRangeRemoved(startPosition, totalItem)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyInsertedItemRange(startPosition: Int, totalItem: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, startPosition)) {
                notifyItemRangeInserted(startPosition, totalItem)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyInsertedItem(position: Int) {
        recyclerView?.isComputingLayout?.let {
            if (isAllowedNotify(it, position)) {
                notifyItemInserted(position)
            } else {
                notifyChangedDataSet()
            }
        }
    }

    private fun notifyChangedDataSet(){
        Handler().post {
            notifyDataSetChanged()
        }
    }

    private fun isAllowedNotify(isComputingLayout: Boolean, position: Int): Boolean {
        return !isComputingLayout && position >= 0
    }

    fun pausePlayCarousel(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ShopHomePlayCarouselViewHolder.ON_PAUSE, true)
        })
    }

    fun resumePlayCarousel(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ShopHomePlayCarouselViewHolder.ON_RESUME, true)
        })
    }

    fun onDestroy(){
        val indexPlay = getPositionPlayCarousel()
        if(indexPlay == -1) return
        notifyItemChanged(indexPlay, Bundle().apply {
            putBoolean(ShopHomePlayCarouselViewHolder.ON_DESTROY, true)
        })
    }

    private fun getPositionPlayCarousel(): Int{
        return visitables.indexOfFirst { it is ShopHomePlayCarouselUiModel}
    }

    fun updateRemindMeStatusCampaignNplWidgetData(
            campaignId: String,
            isRemindMe: Boolean? = null
    ) {
        visitables.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().onEach{nplCampaignUiModel ->
            nplCampaignUiModel.data?.firstOrNull { it.campaignId == campaignId }?.let {
                isRemindMe?.let{ isRemindMe ->
                    it.isRemindMe = isRemindMe
                    if(isRemindMe)
                        ++it.totalNotify
                    else
                        --it.totalNotify
                }
                notifyChangedItem(visitables.indexOf(nplCampaignUiModel))
            }
        }
    }

    fun removeShopHomeCampaignNplWidget(model: ShopHomeNewProductLaunchCampaignUiModel){
        val modelIndex = visitables.indexOf(model)
        if(modelIndex != -1){
            visitables.remove(model)
            notifyRemovedItem(modelIndex)
        }
    }

}
package com.tokopedia.shop.home.view.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.home.view.adapter.viewholder.ShopHomeProductViewHolder
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductEtalaseTitleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.product.view.adapter.scrolllistener.DataEndlessScrollListener
import com.tokopedia.shop.product.view.datamodel.ShopProductSortFilterUiModel
import com.tokopedia.shop.product.view.viewholder.ShopProductSortFilterViewHolder
import com.tokopedia.shop.product.view.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.product.view.widget.StickySingleHeaderView

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeAdapter(
        private val shopHomeAdapterTypeFactory: ShopHomeAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopHomeAdapterTypeFactory>(shopHomeAdapterTypeFactory),
        DataEndlessScrollListener.OnDataEndlessScrollListener,
        StickySingleHeaderView.OnStickySingleHeaderAdapter{

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

    fun setSortFilterData(shopProductSortFilterUiModel: ShopProductSortFilterUiModel) {
        visitables.add(shopProductSortFilterUiModel)
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

    override fun onStickyHide() {
        Handler().post {
            notifyItemChanged(shopProductEtalaseListPosition)
        }
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
        return visitables.indexOfFirst{
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
        notifyItemChanged(visitables.indexOf(shopProductSortFilterUiViewModel))
    }

    fun removeProductList() {
        val firstProductViewModelIndex = visitables.indexOfFirst {
            it::class.java == ShopHomeProductViewModel::class.java
        }
        val totalProductViewModelData = visitables.filterIsInstance<ShopHomeProductViewModel>().size
        if (firstProductViewModelIndex >= 0 && totalProductViewModelData <= visitables.size && firstProductViewModelIndex < totalProductViewModelData) {
            visitables.removeAll(visitables.filterIsInstance<ShopHomeProductViewModel>())
            productListViewModel.clear()
            notifyItemRangeRemoved(firstProductViewModelIndex, totalProductViewModelData)
        }
    }

}
package com.tokopedia.catalog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.catalog.listener.CatalogProductCardListener
import com.tokopedia.catalog.model.raw.CatalogProductItem
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.viewholder.products.CatalogListProductViewHolder
import com.tokopedia.catalog.viewholder.products.CatalogListShimmerModel
import com.tokopedia.common_category.adapter.BaseCategoryAdapter
import com.tokopedia.common_category.factory.BaseProductTypeFactory

class CatalogProductNavListAdapter(private val productTypeFactory: CatalogTypeFactory,
                                   private val visitables: ArrayList<Visitable<CatalogTypeFactory>>,
                                   private val onItemChangeView: OnItemChangeView,
                                   private val catalogProductCardListener: CatalogProductCardListener) : BaseCategoryAdapter(onItemChangeView) {

    private val loadingMoreModel: LoadingMoreModel by lazy { LoadingMoreModel() }

    private val listShimmerModel: CatalogListShimmerModel by lazy { CatalogListShimmerModel() }

    private var currentDimension: String = ""

    private val defaultSortFilterMostAppropriate = "ob=23"

    val viewMap = HashMap<Int, Boolean>()
    var viewedProductList = ArrayList<Visitable<CatalogTypeFactory>>()
    var viewedTopAdsList = ArrayList<Visitable<CatalogTypeFactory>>()
    var isShimmer: Boolean = false

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun setDimension(dimension: String) {
        currentDimension = dimension
    }

    override fun getTypeFactory(): BaseProductTypeFactory {
        return productTypeFactory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        return productTypeFactory.createViewHolder(view, viewType)
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(productTypeFactory)
    }

    fun addShimmer() {
        isShimmer = true
        val item = getShimmerItem()
        for (i in CatalogConstant.SHIMMER_ITEMS_INDEX_START..CatalogConstant.SHIMMER_ITEMS_INDEX_END) {
            this.visitables.add(item as Visitable<CatalogTypeFactory>)
            notifyItemRangeInserted(CatalogConstant.SHIMMER_ITEMS_INDEX_START,CatalogConstant.SHIMMER_ITEMS_INDEX_END + 1)
        }

    }

    private fun getShimmerItem(): Visitable<CatalogTypeFactory> {
        return listShimmerModel
    }

    fun isShimmerRunning(): Boolean {
        return isShimmer
    }

    fun removeShimmer() {
        isShimmer = false
        if (this.visitables.size > CatalogConstant.SHIMMER_ITEMS_INDEX_END) {
            for (i in CatalogConstant.SHIMMER_ITEMS_INDEX_END downTo CatalogConstant.SHIMMER_ITEMS_INDEX_START) {
                this.visitables.removeAt(i)
            }
            notifyItemRangeRemoved(CatalogConstant.SHIMMER_ITEMS_INDEX_START, CatalogConstant.SHIMMER_ITEMS_INDEX_END + 1)
        }

    }


    fun addLoading() {
        val loadingModelPosition = this.visitables.size
        this.visitables.add(loadingMoreModel as Visitable<CatalogTypeFactory>)
        notifyItemInserted(loadingModelPosition)
    }

    fun removeLoading() {
        val loadingModelPosition = this.visitables.indexOf(loadingMoreModel as Visitable<CatalogTypeFactory>)

        if (loadingModelPosition != -1) {
            this.visitables.remove(loadingMoreModel as Visitable<CatalogTypeFactory>)
            notifyItemRemoved(loadingModelPosition)
            notifyItemRangeChanged(loadingModelPosition, 1)
        }
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        visitables.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }

    fun updateWishlistStatus(productId: Int?, isWishlisted: Boolean) {
        for (i in visitables.indices) {
            if (visitables[i] is CatalogProductItem) {
                val model = visitables[i] as CatalogProductItem
                if (productId.toString() == model.id) {
                    model.wishlist = isWishlisted
                    break
                }
            }
        }
    }

    fun setWishlistButtonEnabled(productId: Int?, isEnabled: Boolean) {
        for (i in visitables.indices) {
            if (visitables[i] is CatalogProductItem) {
                val model = visitables[i] as CatalogProductItem
                if (productId.toString() == model.id) {
                    model.isWishListEnabled = isEnabled
                    break
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is CatalogListProductViewHolder) {
            val position = holder.adapterPosition
            if (!viewMap.containsKey(position)) {
                viewMap[position] = true
                val item = visitables[position] as CatalogProductItem
                item.dimension = if (currentDimension.isNotEmpty()) currentDimension else defaultSortFilterMostAppropriate
                item.adapter_position = position

                if (item.isTopAds) {
                    onItemChangeView.topAdsTrackerUrlTrigger(item.productImpTrackingUrl, item.id, item.name, item.imageUrl)
                    viewedTopAdsList.add(item)
                } else {
                    viewedProductList.add(item)
                }
                catalogProductCardListener.onProductImpressed(item,holder.adapterPosition)
            }
        }
    }

    fun onPause() {
        if (viewedProductList.isNotEmpty() || viewedTopAdsList.isNotEmpty()) {
            onItemChangeView.onListItemImpressionEvent(viewedProductList as List<Visitable<Any>>, viewedTopAdsList as List<Visitable<Any>>)
        }
        viewedProductList.clear()
        viewedTopAdsList.clear()
    }
}
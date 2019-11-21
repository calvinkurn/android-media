package com.tokopedia.discovery.categoryrevamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.BigListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.GridListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.Productshimmer.model.ListShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.product.ProductCardViewHolder
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.product.ProductTypeFactory


class ProductNavListAdapter(val productTypeFactory: ProductTypeFactory,
                            val visitables: ArrayList<Visitable<ProductTypeFactory>>,
                            val onItemChangeView: OnItemChangeView) : BaseCategoryAdapter(onItemChangeView) {

    private val loadingMoreModel: LoadingMoreModel  by lazy { LoadingMoreModel() }

    private val listShimmerModel: ListShimmerModel by lazy { ListShimmerModel() }

    private val gridShimmerModelGrid: GridListShimmerModel by lazy { GridListShimmerModel() }

    private val bigListShimmerModel: BigListShimmerModel by lazy { BigListShimmerModel() }

    val viewMap = HashMap<Int, Boolean>()
    var viewedProductList = ArrayList<Visitable<ProductTypeFactory>>()
    var viewedTopAdsList = ArrayList<Visitable<ProductTypeFactory>>()
    var isShimmer: Boolean = false


    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
    }

    override fun getTypeFactory(): BaseProductTypeFactory {
        return productTypeFactory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        val viewHolder = productTypeFactory.createViewHolder(view, viewType)
        return viewHolder
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
        for (i in 0..5) {
            this.visitables.add(item as Visitable<ProductTypeFactory>)
            notifyItemInserted(i)
        }

    }

    private fun getShimmerItem(): Visitable<ProductTypeFactory> {
        return when (getCurrentLayoutType()) {
            CategoryNavConstants.RecyclerView.GridType.GRID_1 -> {
                listShimmerModel
            }

            CategoryNavConstants.RecyclerView.GridType.GRID_2 -> {
                gridShimmerModelGrid
            }
            CategoryNavConstants.RecyclerView.GridType.GRID_3 -> {
                bigListShimmerModel
            }
        }
    }

    fun isShimmerRunning(): Boolean {
        return isShimmer
    }

    fun removeShimmer() {
        isShimmer = false
        if (this.visitables.size > 5) {
            for (i in 5 downTo 0) {
                this.visitables.removeAt(i)
            }
            notifyItemRangeRemoved(0, 6)
        }

    }


    fun addLoading() {
        val loadingModelPosition = this.visitables.size
        this.visitables.add(loadingMoreModel as Visitable<ProductTypeFactory>)
        notifyItemInserted(loadingModelPosition)
    }

    fun removeLoading() {
        val loadingModelPosition = this.visitables.indexOf(loadingMoreModel as Visitable<ProductTypeFactory>)

        if (loadingModelPosition != -1) {
            this.visitables.remove(loadingMoreModel as Visitable<ProductTypeFactory>)
            notifyItemRemoved(loadingModelPosition)
            notifyItemRangeChanged(loadingModelPosition, 1)
        }
    }

    fun clearData() {
        val itemSizeBeforeCleared = itemCount
        visitables.clear()
        notifyItemRangeRemoved(0, itemSizeBeforeCleared)
    }

    fun updateWishlistStatus(productId: Int, isWishlisted: Boolean) {
        for (i in visitables.indices) {
            if (visitables.get(i) is ProductsItem) {
                val model = visitables.get(i) as ProductsItem
                if (productId == model.id) {
                    model.wishlist = isWishlisted
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun setWishlistButtonEnabled(productId: Int, isEnabled: Boolean) {
        for (i in visitables.indices) {
            if (visitables.get(i) is ProductsItem) {
                val model = visitables.get(i) as ProductsItem
                if (productId == model.id) {
                    if (isEnabled && model.isTopAds) {
                        model.productWishlistTrackingUrl?.let {
                            onItemChangeView.wishListEnabledTracker(it)
                        }
                    }
                    model.isWishListEnabled = isEnabled
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is ProductCardViewHolder) {
            val position = holder.adapterPosition
            if (!viewMap.containsKey(position)) {
                viewMap[position] = true
                onItemChangeView.onListItemImpressionEvent(visitables[position] as Visitable<Any>,position)
            }

        }
    }

    private fun getProductItemPath(path: String, id: String): String {
        if (path.isNotEmpty()) {
            return "category$path-$id"
        }
        return ""
    }

    fun onPause() {
        if (viewedProductList.isNotEmpty() || viewedTopAdsList.isNotEmpty()) {
            onItemChangeView.onListItemImpressionEvent(viewedProductList as List<Visitable<Any>>, viewedTopAdsList as List<Visitable<Any>>)
        }
        viewedProductList.clear()
        viewedTopAdsList.clear()
    }
}
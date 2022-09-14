package com.tokopedia.common_category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.BaseProductTypeFactory
import com.tokopedia.common_category.factory.ProductTypeFactory
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.shimmer.BigListShimmerModel
import com.tokopedia.common_category.model.shimmer.GridListShimmerModel
import com.tokopedia.common_category.model.shimmer.ListShimmerModel
import com.tokopedia.common_category.viewholders.ProductCardViewHolder



class ProductNavListAdapter(val productTypeFactory: ProductTypeFactory,
                            val visitables: ArrayList<Visitable<ProductTypeFactory>>,
                            val onItemChangeView: OnItemChangeView) : BaseCategoryAdapter(onItemChangeView) {

    private val loadingMoreModel: LoadingMoreModel by lazy { LoadingMoreModel() }

    private val listShimmerModel: ListShimmerModel by lazy { ListShimmerModel() }

    private val gridShimmerModelGrid: GridListShimmerModel by lazy { GridListShimmerModel() }

    private val bigListShimmerModel: BigListShimmerModel by lazy { BigListShimmerModel() }

    private var currentDimension: String = ""
    private val defaultSortFilterMostAppropriate = "ob=23"

    val viewMap = HashMap<Int, Boolean>()
    var viewedProductList = ArrayList<Visitable<ProductTypeFactory>>()
    var viewedTopAdsList = ArrayList<Visitable<ProductTypeFactory>>()
    var isShimmer: Boolean = false


    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        val layout = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        when(visitables[position]){
            is LoadingMoreModel -> layout.isFullSpan = true
        }
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

    fun updateWishlistStatus(productId: String, isWishlisted: Boolean) {
        for (i in visitables.indices) {
            if (visitables[i] is ProductsItem) {
                val model = visitables[i] as ProductsItem
                if (productId == model.id) {
                    model.wishlist = isWishlisted
                    notifyItemChanged(i)
                    break
                }
            }
        }
    }

    fun setWishlistButtonEnabled(productId: String, isEnabled: Boolean) {
        for (i in visitables.indices) {
            if (visitables[i] is ProductsItem) {
                val model = visitables[i] as ProductsItem
                if (productId == model.id) {
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
                val item = visitables[position] as ProductsItem
                item.dimension = if (currentDimension.isNotEmpty()) currentDimension else defaultSortFilterMostAppropriate
                item.adapter_position = position

                if (item.isTopAds) {
                    onItemChangeView.topAdsTrackerUrlTrigger(item.productImpTrackingUrl, item.id?.toString() ?: "", item.name, item.imageURL)
                    viewedTopAdsList.add(item)
                } else {
                    viewedProductList.add(item)
                }
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
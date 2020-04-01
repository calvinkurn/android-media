package com.tokopedia.discovery.categoryrevamp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.BigListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.GridListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.adapters.viewHolders.CatalogShimmer.model.ListCatalogShimmerModel
import com.tokopedia.discovery.categoryrevamp.constants.CategoryNavConstants
import com.tokopedia.discovery.categoryrevamp.data.typefactory.BaseProductTypeFactory
import com.tokopedia.discovery.categoryrevamp.data.typefactory.catalog.CatalogTypeFactory


class CatalogNavListAdapter(val catalogTypeFactory: CatalogTypeFactory,
                            val visitables: ArrayList<Visitable<CatalogTypeFactory>>,
                            onItemChangeView: OnItemChangeView) : BaseCategoryAdapter(onItemChangeView) {

    private var loadingMoreModel: LoadingMoreModel = LoadingMoreModel()

    private val listShimmerModel: ListCatalogShimmerModel by lazy { ListCatalogShimmerModel() }

    private val gridShimmerModelGrid: GridListCatalogShimmerModel by lazy { GridListCatalogShimmerModel() }

    private val bigListShimmerModel: BigListCatalogShimmerModel by lazy { BigListCatalogShimmerModel() }

    var isShimmer: Boolean = false

    override fun setDimension(dimension: String) {
    }

    override fun getTypeFactory(): BaseProductTypeFactory {
        return catalogTypeFactory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<Visitable<*>> {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(viewType, parent, false)
        val viewHolder = catalogTypeFactory.createViewHolder(view, viewType)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return visitables.size
    }

    override fun getItemViewType(position: Int): Int {
        return visitables[position].type(catalogTypeFactory)
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<Visitable<*>>, position: Int) {
        holder.bind(visitables[position])
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

    fun addShimmer() {
        isShimmer = true
        val item = getShimmerItem()
        for (i in 0..5) {
            this.visitables.add(item as Visitable<CatalogTypeFactory>)
            notifyItemInserted(i)
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


    private fun getShimmerItem(): Visitable<CatalogTypeFactory> {
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
}

package com.tokopedia.common_category.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common_category.constants.CategoryNavConstants
import com.tokopedia.common_category.factory.BaseProductTypeFactory

abstract class BaseCategoryAdapter(val itemChangeView: OnItemChangeView) :
        RecyclerView.Adapter<AbstractViewHolder<Visitable<*>>>() {

    abstract fun setDimension(dimension:String)

    fun changeListView() {
        getTypeFactory().setRecyclerViewItem(CategoryNavConstants.RecyclerView.VIEW_PRODUCT)
        itemChangeView.onChangeList()
    }

    fun changeDoubleGridView() {
        getTypeFactory().setRecyclerViewItem(CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_2)
        itemChangeView.onChangeDoubleGrid()
    }

    fun changeSingleGridView() {
        getTypeFactory().setRecyclerViewItem(CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_1)
        itemChangeView.onChangeSingleGrid()
    }


    fun getCurrentLayoutType(): CategoryNavConstants.RecyclerView.GridType {
        return when (getTypeFactory().getRecyclerViewItem()) {
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT -> CategoryNavConstants.RecyclerView.GridType.GRID_1
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_2 -> CategoryNavConstants.RecyclerView.GridType.GRID_2
            CategoryNavConstants.RecyclerView.VIEW_PRODUCT_GRID_1 -> CategoryNavConstants.RecyclerView.GridType.GRID_3
            else -> CategoryNavConstants.RecyclerView.GridType.GRID_2
        }
    }

    protected abstract fun getTypeFactory(): BaseProductTypeFactory


    interface OnItemChangeView {
        fun onChangeList()

        fun onChangeDoubleGrid()

        fun onChangeSingleGrid()

        fun onListItemImpressionEvent(viewedProductList: List<Visitable<Any>>,viewedTopAdsList: List<Visitable<Any>>)

        fun topAdsTrackerUrlTrigger(url: String, id: String, name: String, imageURL: String)
    }

}

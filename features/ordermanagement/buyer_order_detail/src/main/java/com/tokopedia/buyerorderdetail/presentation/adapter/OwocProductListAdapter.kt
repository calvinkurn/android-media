package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.OwocProductListDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.OwocProductListTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.BaseOwocVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel

class OwocProductListAdapter(private val typeFactory: OwocProductListTypeFactoryImpl) :
    BaseAdapter<OwocProductListTypeFactoryImpl>(typeFactory) {

    fun updateItems(newList: List<BaseOwocVisitableUiModel>?) {
        if (newList.isNullOrEmpty()) return
        val diffCallback = OwocProductListDiffUtilCallback(
            visitables as List<Visitable<OwocProductListTypeFactoryImpl>>,
            newList,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        visitables.clear()
        visitables.addAll(newList)
    }
    fun collapseOwocProduct(expandProducts: List<BaseOwocVisitableUiModel>, isExpanded: Boolean) {

        val collapseItems = list.toMutableList()

        val newProductListToggleUiModel =
            filterUiModel<OwocProductListUiModel.ProductListToggleUiModel>()?.copy(isExpanded = isExpanded)

        val productListToggleIndex =
            list.indexOfFirst { it is OwocProductListUiModel.ProductListToggleUiModel }
                .takeIf { it > RecyclerView.NO_POSITION }

        productListToggleIndex?.let {
            collapseItems.set(
                it,
                newProductListToggleUiModel
            )
        }
        collapseItems.removeAll(expandProducts)
        val collapseNewItems =
            collapseItems as? List<BaseOwocVisitableUiModel>

        updateItems(collapseNewItems?.toList())
    }

    fun expandOwocProduct(expandProducts: List<BaseOwocVisitableUiModel>, isExpanded: Boolean) {
        val expandItems = list.toMutableList()

        val newProductListToggleUiModel =
            filterUiModel<OwocProductListUiModel.ProductListToggleUiModel>()?.copy(isExpanded = isExpanded)
        val productListToggleIndex =
            list.indexOfFirst { it is OwocProductListUiModel.ProductListToggleUiModel }
                .takeIf { it > RecyclerView.NO_POSITION }

        productListToggleIndex?.let {
            expandItems.set(
                it,
                newProductListToggleUiModel
            )

            expandItems.addAll(productListToggleIndex, expandProducts)
            val expandNewItems = expandItems as? List<BaseOwocVisitableUiModel>
            updateItems(expandNewItems?.toList())
        }
    }

    inline fun <reified T : Visitable<*>> filterUiModel(): T? {
        return list.filterIsInstance<T>().firstOrNull()
    }
}

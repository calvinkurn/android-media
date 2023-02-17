package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.PartialOrderFulfillmentDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofFulfilledToggleUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero

class PartialOrderFulfillmentAdapter(
    private val partialOrderFulfillmentTypeFactoryImpl: PartialOrderFulfillmentTypeFactoryImpl
) : BaseAdapter<PartialOrderFulfillmentTypeFactoryImpl>(partialOrderFulfillmentTypeFactoryImpl) {

    fun updateItems(newPofList: List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>) {
        val diffCallback = PartialOrderFulfillmentDiffUtilCallback(
            visitables as List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>,
            newPofList,
            partialOrderFulfillmentTypeFactoryImpl
        )

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)

        visitables.clear()
        visitables.addAll(newPofList)
    }

    fun collapseFulfilledProducts(isExpanded: Boolean) {
        val fulFilledProductSize = list.filterIsInstance<PofProductFulfilledUiModel>().size
        if (fulFilledProductSize.isMoreThanZero()) {
            val collapseItems = list.toMutableList()

            val newPofFulfilledToggleUiModel = filterUiModel<PofFulfilledToggleUiModel>()
                ?.copy(isExpanded = isExpanded)
            val pofFulfilledToggleIndex = list
                .indexOfFirst { it is PofFulfilledToggleUiModel }
                .takeIf { it != RecyclerView.NO_POSITION }

            pofFulfilledToggleIndex?.let {
                collapseItems.set(
                    it,
                    newPofFulfilledToggleUiModel
                )
            }
            collapseItems.removeAll { it is PofProductFulfilledUiModel }
            val collapseNewItems =
                collapseItems as List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>
            updateItems(collapseNewItems)
        }
    }

    fun expandFulfilledProducts(fulfilledProducts: List<PofProductFulfilledUiModel>, isExpanded: Boolean) {
        val fulfilledProductIndex = list.indexOfFirst { it is PofFulfilledToggleUiModel } + Int.ONE
        val expandItems = list.toMutableList()

        val newPofFulfilledToggleUiModel = filterUiModel<PofFulfilledToggleUiModel>()
            ?.copy(isExpanded = isExpanded)
        val pofFulfilledToggleIndex = list
            .indexOfFirst { it is PofFulfilledToggleUiModel }
            .takeIf { it != RecyclerView.NO_POSITION }

        pofFulfilledToggleIndex?.let {
            expandItems.set(
                it,
                newPofFulfilledToggleUiModel
            )
        }
        expandItems.addAll(fulfilledProductIndex, fulfilledProducts)
        val expandNewItems = expandItems as List<Visitable<PartialOrderFulfillmentTypeFactoryImpl>>
        updateItems(expandNewItems)
    }

    fun showLoadingShimmer() {
        if (visitables.getOrNull(Int.ZERO) !is LoadingModel) {
            visitables.add(LoadingModel())
            notifyItemInserted(Int.ZERO)
        }
    }

    fun hideLoadingShimmer() {
        if (visitables.getOrNull(lastIndex) is LoadingModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun showError(item: PofErrorUiModel) {
        if (visitables.getOrNull(Int.ZERO) !is PofErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(Int.ZERO)
        }
    }

    fun hideError() {
        if (visitables.getOrNull(lastIndex) is PofErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    inline fun <reified T : Visitable<*>> filterUiModel(): T? {
        return list.filterIsInstance<T>().firstOrNull()
    }
}

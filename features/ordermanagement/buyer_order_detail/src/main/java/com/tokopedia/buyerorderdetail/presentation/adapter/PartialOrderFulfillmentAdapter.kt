package com.tokopedia.buyerorderdetail.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.buyerorderdetail.presentation.adapter.diffutil.PartialOrderFulfillmentDiffUtilCallback
import com.tokopedia.buyerorderdetail.presentation.adapter.typefactory.PartialOrderFulfillmentTypeFactoryImpl
import com.tokopedia.buyerorderdetail.presentation.model.BasePofVisitableUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PofErrorUiModel
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

        visitables.clear()
        visitables.addAll(newPofList)

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(
        oldItem: BasePofVisitableUiModel,
        newItem: BasePofVisitableUiModel
    ) {
        val index = visitables.indexOf(oldItem)
        if (index != RecyclerView.NO_POSITION) {
            visitables[index] = newItem
            notifyItemChanged(index, oldItem to newItem)
        }
    }

    fun collapseFulfilledProducts() {
        val fulFilledProductSize = visitables.filterIsInstance<PofProductFulfilledUiModel>().size
        if (fulFilledProductSize.isMoreThanZero()) {
            visitables.removeAll { it is PofProductFulfilledUiModel }
            notifyItemRangeRemoved(visitables.size, fulFilledProductSize)
        }
    }

    fun expandFulfilledProducts(fulfilledProducts: List<PofProductFulfilledUiModel>) {
        val fulfilledProductIndex =
            visitables.indexOfFirst { it is PofProductFulfilledUiModel } + Int.ONE
        visitables.addAll(fulfilledProductIndex, fulfilledProducts)
        notifyItemRangeInserted(fulfilledProductIndex, fulfilledProducts.size)
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

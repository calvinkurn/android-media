package com.tokopedia.sellerorder.list.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks.SomListOrderDiffUtilCallback
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
import com.tokopedia.sellerorder.list.presentation.models.SomListMultiSelectSectionUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListOrderAdapter(
    adapterTypeFactory: SomListAdapterTypeFactory
) : BaseListAdapter<Visitable<SomListAdapterTypeFactory>, SomListAdapterTypeFactory>(
    adapterTypeFactory
) {

    override fun removeErrorNetwork() {
        val newVisitableList = visitables.filterNot { it == errorNetworkModel }
        updateOrders(newVisitableList)
    }

    override fun showLoading() {
        if (!isLoading) {
            if (isShowLoadingMore) {
                updateOrders(visitables.plus(loadingMoreModel))
            } else {
                updateOrders(visitables.plus(loadingModel))
            }
        }
    }

    override fun hideLoading() {
        val newVisitableList = visitables.filterNot { it is LoadingModel || it is LoadingMoreModel }
        updateOrders(newVisitableList)
    }

    fun updateOrders(items: List<Visitable<*>>) {
        val diffCallback = SomListOrderDiffUtilCallback(visitables.toMutableList(), items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }


    fun updateOrder(newOrder: SomListOrderUiModel) {
        visitables.map {
            if (it is SomListOrderUiModel && it.orderId == newOrder.orderId) {
                newOrder
            } else {
                it
            }
        }.let { updateOrders(it) }
    }

    fun removeOrder(orderId: String) {
        visitables.filter {
            it is SomListOrderUiModel && it.orderId != orderId || it !is SomListOrderUiModel
        }.let { updateOrders(it) }
    }

    fun updateSomListMultiSelectSectionUiModel(
        somListMultiSelectSectionUiModel: SomListMultiSelectSectionUiModel
    ) {
        val newItems = if (visitables.any { it is SomListMultiSelectSectionUiModel }) {
            visitables.map {
                if (it is SomListMultiSelectSectionUiModel) somListMultiSelectSectionUiModel else it
            }
        } else {
            ArrayList(visitables).apply {
                add(Int.ZERO, somListMultiSelectSectionUiModel)
            }
        }
        updateOrders(newItems)
    }

    fun hasOrder(): Boolean {
        return visitables.any { it is SomListOrderUiModel }
    }

    fun removeMultiSelectSection() {
        visitables.filter {
            it !is SomListMultiSelectSectionUiModel
        }.let { updateOrders(it) }
    }

    fun resetOrderSelectedStatus(somListMultiSelectSectionUiModel: SomListMultiSelectSectionUiModel) {
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel) {
                it.copy(isChecked = false, multiSelectEnabled = somListMultiSelectSectionUiModel.isEnabled)
            } else if (it is SomListMultiSelectSectionUiModel) {
                somListMultiSelectSectionUiModel
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }

    fun checkAllOrder(somListMultiSelectSectionUiModel: SomListMultiSelectSectionUiModel) {
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel) {
                it.copy(isChecked = !it.isOrderWithCancellationRequest())
            } else if (it is SomListMultiSelectSectionUiModel) {
                somListMultiSelectSectionUiModel
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }
}
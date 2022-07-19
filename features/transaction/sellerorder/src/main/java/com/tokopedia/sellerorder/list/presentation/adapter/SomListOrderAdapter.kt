package com.tokopedia.sellerorder.list.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks.SomListOrderDiffUtilCallback
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListAdapterTypeFactory
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
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel && it.orderId == newOrder.orderId) {
                newOrder
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }

    fun removeOrder(orderId: String) {
        val newVisitableList = visitables.filter {
            it is SomListOrderUiModel && it.orderId != orderId || it !is SomListOrderUiModel
        }
        updateOrders(newVisitableList)
    }

    fun resetOrderSelectedStatus() {
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel) {
                it.copy(isChecked = false)
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }

    fun checkAllOrder() {
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel) {
                it.copy(isChecked = true)
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }

    fun updateMultiSelect(multiSelectEnabled: Boolean) {
        val newVisitableList = visitables.map {
            if (it is SomListOrderUiModel) {
                it.copy(isChecked = false, multiSelectEnabled = multiSelectEnabled)
            } else {
                it
            }
        }
        updateOrders(newVisitableList)
    }
}
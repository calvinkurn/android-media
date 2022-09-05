package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.diffutil.OrderTrackingDiffUtilCallback
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingEstimationUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingStatusInfoUiModel

class OrderTrackingAdapter(
    private val orderTrackingAdapterTypeFactory: OrderTrackingAdapterTypeFactoryImpl
) : BaseAdapter<OrderTrackingAdapterTypeFactoryImpl>(orderTrackingAdapterTypeFactory) {

    fun updateOrderTracking(newItems: List<BaseOrderTrackingTypeFactory>) {
        val oldItems = visitables.filterIsInstance<BaseOrderTrackingTypeFactory>()
        val diffCallback = OrderTrackingDiffUtilCallback(
            oldItems, newItems,
            orderTrackingAdapterTypeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(
        oldItem: BaseOrderTrackingTypeFactory,
        newItem: BaseOrderTrackingTypeFactory
    ) {
        val index = visitables.indexOf(oldItem)
        if (index != RecyclerView.NO_POSITION) {
            visitables[index] = newItem
            notifyItemChanged(index, oldItem to newItem)
        }
    }

    fun removeOrderTrackingData() {
        val orderTrackingDataCount = visitables.filterIsInstance<BaseOrderTrackingTypeFactory>().count()
        if (orderTrackingDataCount.isMoreThanZero()) {
            visitables.removeAll { it is BaseOrderTrackingTypeFactory }
            notifyItemRangeRemoved(visitables.size, orderTrackingDataCount)
        }
    }

    fun updateEtaLiveTracking(newEtaItem: OrderTrackingEstimationUiModel?) {
        if (newEtaItem != null) {
            val index = visitables.indexOfFirst { it is OrderTrackingEstimationUiModel }
            if (index == RecyclerView.NO_POSITION) {
                val liveTrackingStatusIndex =
                    visitables.indexOfFirst { it is OrderTrackingStatusInfoUiModel }
                val newEtaIndex = liveTrackingStatusIndex + Int.ONE
                visitables.add(newEtaIndex, newEtaItem)
                notifyItemInserted(newEtaIndex)
            }
        }
    }

    fun expandOrderDetail(newFoodItemList: List<BaseOrderTrackingTypeFactory>) {
        val foodItemCount = visitables.filterIsInstance<FoodItemUiModel>().count()
        if (foodItemCount == Int.ONE) {
            val foodItemFirstIndex = visitables.indexOfFirst { it is FoodItemUiModel } + Int.ONE
            visitables.addAll(foodItemFirstIndex, newFoodItemList)
            notifyItemRangeInserted(foodItemFirstIndex, newFoodItemList.size)
        }
    }

    fun collapseOrderDetail() {
        val foodItemCount = visitables.filterIsInstance<FoodItemUiModel>().count()
        if (foodItemCount > Int.ONE) {
            val foodItemFirstPos = visitables.indexOfFirst { it is FoodItemUiModel } + Int.ONE
            val foodItemLastPos = visitables.indexOfLast { it is FoodItemUiModel } + Int.ONE
            val removedCount = foodItemLastPos - foodItemFirstPos
            visitables.subList(foodItemFirstPos, foodItemLastPos).clear()
            notifyItemRangeRemoved(foodItemFirstPos, removedCount)
        }
    }

    fun showError(item: OrderTrackingErrorUiModel) {
        if (visitables.getOrNull(FIRST_INDEX) !is OrderTrackingErrorUiModel) {
            visitables.add(item)
            notifyItemInserted(FIRST_INDEX)
        }
    }

    fun hideError() {
        if (visitables.getOrNull(lastIndex) is OrderTrackingErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun showLoadingShimmer(item: LoadingModel) {
        if (visitables.getOrNull(FIRST_INDEX) !is LoadingModel) {
            visitables.add(item)
            notifyItemInserted(FIRST_INDEX)
        }
    }

    fun hideLoadingShimmer() {
        if (visitables.getOrNull(lastIndex) is LoadingModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    inline fun <reified T: BaseOrderTrackingTypeFactory> updateLiveTrackingItem(newItem: T?) {
        if (newItem != null) {
            val oldItem = filterUiModel<T>()
            oldItem?.let {
                updateItem(oldItem, newItem)
            }
        }
    }

    inline fun <reified T: Visitable<*>> filterUiModel(): T? {
        return list.filterIsInstance<T>().firstOrNull()
    }

    companion object {
        const val FIRST_INDEX = 0
    }
}
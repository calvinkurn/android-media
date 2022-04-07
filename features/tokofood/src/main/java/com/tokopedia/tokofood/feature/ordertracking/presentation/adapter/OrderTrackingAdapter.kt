package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.diffutil.OrderTrackingDiffUtilCallback
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderTrackingErrorUiModel

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
        oldItem: Visitable<BaseOrderTrackingTypeFactory>,
        newItem: Visitable<BaseOrderTrackingTypeFactory>
    ) {
        val index = visitables.indexOf(oldItem)
        if (index != RecyclerView.NO_POSITION) {
            visitables[index] = newItem
            notifyItemChanged(index, oldItem to newItem)
        }
    }

    fun expandOrderDetail(newFoodItemList: List<BaseOrderTrackingTypeFactory>) {
        val foodItemCount = visitables.filterIsInstance<FoodItemUiModel>().count()
        if (foodItemCount == Int.ONE) {
            val foodItemFirstIndex = visitables.indexOfFirst { it is FoodItemUiModel }
            visitables.addAll(foodItemFirstIndex, newFoodItemList)
            notifyItemRangeInserted(foodItemFirstIndex, newFoodItemList.size)
        }
    }

    fun collapseOrderDetail() {
        val foodItemCount = visitables.filterIsInstance<FoodItemUiModel>().count()
        if (foodItemCount > Int.ONE) {
            val foodItemFirstIndex = visitables.indexOfFirst { it is FoodItemUiModel }
            val foodItemLastIndex = visitables.indexOfLast { it is FoodItemUiModel }
            val startRemovedIndex = foodItemFirstIndex + Int.ONE
            val removedCount = foodItemLastIndex - foodItemFirstIndex
            visitables.subList(startRemovedIndex, foodItemLastIndex).clear()
            notifyItemRangeRemoved(startRemovedIndex, removedCount)
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

    companion object {
        const val FIRST_INDEX = 0
    }
}
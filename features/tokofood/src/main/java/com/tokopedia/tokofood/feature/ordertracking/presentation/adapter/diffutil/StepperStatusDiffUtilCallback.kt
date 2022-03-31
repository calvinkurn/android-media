package com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.diffutil

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.StepperStatusAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.StepperStatusUiModel

class StepperStatusDiffUtilCallback(private val oldItems: List<StepperStatusUiModel>,
                                    private val newItems: List<StepperStatusUiModel>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition)?.isActive == newItems.getOrNull(newItemPosition)?.isActive
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition) == newItems.getOrNull(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val newItem = newItems.getOrNull(newItemPosition)
        val oldItem = oldItems.getOrNull(oldItemPosition)

        val diff = Bundle()

        newItem?.let {
            if (it.isActive != oldItem?.isActive) {
                diff.putBoolean(StepperStatusAdapter.KEY_IS_ACTIVE, it.isActive)
            }
        }

        return if (diff.size().isMoreThanZero()) diff else null
    }
}
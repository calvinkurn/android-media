package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.InitialStateTypeFactoryImpl
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.BaseInitialStateTypeFactory

class InitialSearchStateDiffUtil(
    private val oldItems: List<BaseInitialStateTypeFactory>,
    private val newItems: List<BaseInitialStateTypeFactory>,
    private val initialStateTypeFactoryImpl: InitialStateTypeFactoryImpl
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition)
            ?.type(initialStateTypeFactoryImpl) == newItems.getOrNull(newItemPosition)
            ?.type(initialStateTypeFactoryImpl)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition) == newItems.getOrNull(newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }
}
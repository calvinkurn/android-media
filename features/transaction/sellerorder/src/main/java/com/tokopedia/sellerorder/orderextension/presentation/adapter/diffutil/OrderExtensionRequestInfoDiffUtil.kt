package com.tokopedia.sellerorder.orderextension.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory.OrderExtensionRequestInfoAdapterTypeFactory
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoDiffUtil(
    private val oldItems: List<Visitable<OrderExtensionRequestInfoAdapterTypeFactory>>,
    private val newItems: List<Visitable<OrderExtensionRequestInfoAdapterTypeFactory>>,
    private val typeFactory: OrderExtensionRequestInfoAdapterTypeFactory
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getUniqueIdentifier(oldItems.getOrNull(oldItemPosition)) == getUniqueIdentifier(newItems.getOrNull(
            newItemPosition))
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return getUniqueIdentifier(oldItems.getOrNull(oldItemPosition)) == getUniqueIdentifier(newItems.getOrNull(
            newItemPosition))
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return Pair(oldItem, newItem)
    }

    private fun getUniqueIdentifier(type: Visitable<OrderExtensionRequestInfoAdapterTypeFactory>?): Int {
        val model = type as? OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem
        if (model is OrderExtensionRequestInfoUiModel.DescriptionUiModel) {
            return model.id
        }
        return model?.type(typeFactory).orZero()
    }
}

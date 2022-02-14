package com.tokopedia.sellerorder.orderextension.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.diffutil.OrderExtensionRequestInfoDiffUtil
import com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory.OrderExtensionRequestInfoAdapterTypeFactory
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.BaseOrderExtensionRequestInfoViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel

class OrderExtensionRequestInfoAdapter(
    val typeFactory: OrderExtensionRequestInfoAdapterTypeFactory
) : BaseAdapter<OrderExtensionRequestInfoAdapterTypeFactory>(typeFactory) {

    override fun onViewAttachedToWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewAttachedToWindow(holder)
        if (holder is BaseOrderExtensionRequestInfoViewHolder) {
            holder.onViewAttachedFromWindow()
        }
    }

    override fun onViewDetachedFromWindow(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewDetachedFromWindow(holder)
        if (holder is BaseOrderExtensionRequestInfoViewHolder) {
            holder.onViewDetachedFromWindow()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun processDiff(newItems: List<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>) {
        val diffCallback = OrderExtensionRequestInfoDiffUtil(
            visitables as List<Visitable<OrderExtensionRequestInfoAdapterTypeFactory>>,
            newItems,
            typeFactory
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun updateVisitable(newItems: List<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>) {
        visitables.clear()
        visitables.addAll(newItems)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findIndexOfFirstRequestFocusItem(): Int {
        return (visitables as List<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>).indexOfFirst {
            it.requestFocus
        }
    }

    private fun setRequestFocusAsFalse(index: Int) {
        (visitables.getOrNull(index) as? OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem)?.requestFocus = false
    }

    fun updateItems(newItems: List<OrderExtensionRequestInfoUiModel.BaseOrderExtensionRequestInfoItem>) {
        processDiff(newItems)
        updateVisitable(newItems)
    }

    fun getRequestFocusItemPosition(): Int {
        return findIndexOfFirstRequestFocusItem().also {
            setRequestFocusAsFalse(it)
        }
    }
}
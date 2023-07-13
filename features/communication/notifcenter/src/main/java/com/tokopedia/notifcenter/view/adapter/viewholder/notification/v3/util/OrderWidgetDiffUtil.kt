package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.entity.orderlist.OrderWidgetUiModel

class OrderWidgetDiffUtil(
    private val old: List<Visitable<*>>,
    private val new: List<Visitable<*>>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition] as? OrderWidgetUiModel
        val newItem = new[newItemPosition] as? OrderWidgetUiModel
        return oldItem?.text == newItem?.text
    }

    /**
     * Assuming the visitable is data class
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return oldItem == newItem
    }

}

package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel

/**
 * @author Said Faisal on 24/11/2021
 */

class MoreMenuDiffer: DiffUtil.Callback() {
    private var oldList: List<MoreMenuUiModel> = emptyList()
    private var newList: List<MoreMenuUiModel> = emptyList()

    /**
     * @see areItemsTheSame
     *
     * This differ based on keyword of items
     **/
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): MoreMenuDiffer {
        this.oldList = oldList as List<MoreMenuUiModel>
        this.newList = newList as List<MoreMenuUiModel>
        return this
    }
}
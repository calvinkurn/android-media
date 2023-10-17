package com.tokopedia.minicart.bmgm.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 17/10/23.
 */

class MiniCartDiffUtilCallback(
    private val oldList: List<BmgmMiniCartVisitable>,
    private val newList: List<BmgmMiniCartVisitable>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList.getOrNull(oldItemPosition)
        val new = newList.getOrNull(newItemPosition)
        return old?.getItemId() == new?.getItemId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList.getOrNull(oldItemPosition)
        val new = newList.getOrNull(newItemPosition)
        return old?.toString() == new?.toString()
    }
}
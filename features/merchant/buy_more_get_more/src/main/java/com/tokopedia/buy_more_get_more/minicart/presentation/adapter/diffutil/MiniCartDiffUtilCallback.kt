package com.tokopedia.buy_more_get_more.minicart.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BaseMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable

/**
 * Created by @ilhamsuaib on 17/10/23.
 */

class MiniCartDiffUtilCallback(
    private val oldList: List<BaseMiniCartVisitable>,
    private val newList: List<BaseMiniCartVisitable>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList.getOrNull(oldItemPosition) ?: return false
        val new = newList.getOrNull(newItemPosition) ?: return false
        return old.getItemId() == new.getItemId()
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList.getOrNull(oldItemPosition) ?: return false
        val new = newList.getOrNull(newItemPosition) ?: return false
        return old == new
    }
}
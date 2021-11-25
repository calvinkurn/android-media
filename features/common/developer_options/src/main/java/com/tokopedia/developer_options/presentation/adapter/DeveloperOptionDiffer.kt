package com.tokopedia.developer_options.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.developer_options.presentation.model.OptionItemUiModel

/**
 * @author Said Faisal on 24/11/2021
 */

class DeveloperOptionDiffer: DiffUtil.Callback() {
    private var oldList: List<OptionItemUiModel> = emptyList()
    private var newList: List<OptionItemUiModel> = emptyList()

    /**
     * @see areItemsTheSame
     *
     * This differ based on keyword of items
     **/
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.keyword == newItem.keyword
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): DeveloperOptionDiffer {
        this.oldList = oldList as List<OptionItemUiModel>
        this.newList = newList as List<OptionItemUiModel>
        return this
    }
}
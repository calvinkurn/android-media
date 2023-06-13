package com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsPopularSearch

class ChipsHighlightDiffUtil(private val oldList: List<ChipsPopularSearch>,
                              private val newList: List<ChipsPopularSearch>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title &&
                oldList[oldItemPosition].appLink == newList[newItemPosition].appLink
    }
}
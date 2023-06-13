package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class QuickFilterUiModel(
    val name: String,
    val icon: String,
    val key: String,
    val value: String,
) {
    fun toSortFilterItem(isSelected: Boolean, listener: () -> Unit): SortFilterItem {
        return SortFilterItem(
            title = name,
            listener = listener,
            type = if(isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
        ).apply {
            typeUpdated = false
        }
    }
}
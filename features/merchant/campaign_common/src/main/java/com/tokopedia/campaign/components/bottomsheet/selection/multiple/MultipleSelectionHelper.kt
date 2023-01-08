package com.tokopedia.campaign.components.bottomsheet.selection.multiple

import com.tokopedia.campaign.components.bottomsheet.selection.entity.MultipleSelectionItem

internal class MultipleSelectionHelper {

    private val selectedItemIds = mutableSetOf<String>()

    fun markAsSelected(itemId: String) {
        selectedItemIds.add(itemId)
    }

    fun markAsUnselected(itemId: String) {
        selectedItemIds.remove(itemId)
    }

    fun refresh(items: List<MultipleSelectionItem>): List<MultipleSelectionItem> {
        return items.map { item ->
            if (item.id in selectedItemIds) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }
    }

    fun findSelectedItems(items: List<MultipleSelectionItem>): List<MultipleSelectionItem> {
        return items.filter { item -> item.id in selectedItemIds }
    }

}

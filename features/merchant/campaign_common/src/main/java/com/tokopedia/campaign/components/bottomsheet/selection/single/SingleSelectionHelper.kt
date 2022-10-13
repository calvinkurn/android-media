package com.tokopedia.campaign.components.bottomsheet.selection.single

import com.tokopedia.campaign.components.bottomsheet.selection.entity.SingleSelectionItem

internal class SingleSelectionHelper {

    fun markAsSelected(
        selectedItemId: String,
        items: List<SingleSelectionItem>
    ): List<SingleSelectionItem> {
        return items.map { item ->
            if (item.id == selectedItemId) {
                item.copy(isSelected = true)
            } else {
                item.copy(isSelected = false)
            }
        }
    }

    fun findSelectedItem(items: List<SingleSelectionItem>): SingleSelectionItem? {
        return items.find { item -> item.isSelected }
    }

}

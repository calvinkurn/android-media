package com.tokopedia.campaign.widget.bottomsheet

import com.tokopedia.campaign.entity.SingleSelectionItem

class SingleSelectionHelper {

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

    fun findSelectedItem(vpsPackages: List<SingleSelectionItem>): SingleSelectionItem? {
        return vpsPackages.find { vpsPackage -> vpsPackage.isSelected }
    }

}
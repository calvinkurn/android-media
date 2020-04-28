package com.tokopedia.product.addedit.common.util

import android.view.View
import android.widget.ListView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

internal fun ListItemUnify.getShownRadioButton()= run {
    if (listLeftRadiobtn?.visibility == View.VISIBLE) listLeftRadiobtn
    else if (listRightRadiobtn?.visibility == View.VISIBLE) listRightRadiobtn
    else null
}

internal fun ListUnify.getSelected(items: List<ListItemUnify>) = run {
    when (choiceMode) {
        // for radio button type
        ListView.CHOICE_MODE_SINGLE -> {
            items.firstOrNull { it.listRightRadiobtn?.isChecked ?: false || it.listLeftRadiobtn?.isChecked ?: false }
        }
        else -> {
            null
        }
    }
}

internal fun ListUnify.setSelected(items: List<ListItemUnify>, position: Int, onChecked: (selectedItem: ListItemUnify) -> Any) = run {
    val selectedItem = this.getItemAtPosition(position) as ListItemUnify

    when (choiceMode) {
        // for radio button type
        ListView.CHOICE_MODE_SINGLE -> {
            // deselect previously selected item
            items.filter { it.getShownRadioButton()?.isChecked ?: false }
                    .filterNot { it == selectedItem }
                    .onEach { it.getShownRadioButton()?.isChecked = false }
            selectedItem.getShownRadioButton()?.isChecked = true
        }
    }

    onChecked(selectedItem)
}

internal fun ListItemUnify.getCategoryId() = run {
    listActionText?.toLong().orZero()
}
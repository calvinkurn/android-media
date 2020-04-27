package com.tokopedia.product.addedit.common.util

import android.view.View
import android.widget.ListView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

object ListUnifyUtil {
    @Suppress("UNCHECKED_CAST")
    private fun getItems(instance: ListUnify) = with(instance) {
        this.javaClass.getDeclaredField("array").let {
            it.isAccessible = true
            it.get(this) as ArrayList<ListItemUnify>
        }
    }

    fun getShownRadioButton(instance: ListItemUnify) = with(instance) {
        if (listLeftRadiobtn?.visibility == View.VISIBLE) listLeftRadiobtn
        else if (listRightRadiobtn?.visibility == View.VISIBLE) listRightRadiobtn
        else null
    }

    fun getSelected(instance: ListUnify?) = instance?.run {
        when (choiceMode) {
            // for radio button type
            ListView.CHOICE_MODE_SINGLE -> {
                getItems(this).firstOrNull { it.listRightRadiobtn?.isChecked ?: false || it.listLeftRadiobtn?.isChecked ?: false }
            }
            else -> {
                null
            }
        }
    }

    fun setSelected(instance: ListUnify, position: Int, onChecked: (selectedItem: ListItemUnify) -> Any) = with(instance) {
        val selectedItem = this.getItemAtPosition(position) as ListItemUnify

        when (choiceMode) {
            // for radio button type
            ListView.CHOICE_MODE_SINGLE -> {
                // deselect previously selected item
                getItems(this).filter { getShownRadioButton(it)?.isChecked ?: false }
                        .filterNot { it == selectedItem }
                        .onEach { getShownRadioButton(it)?.isChecked = false }
                getShownRadioButton(selectedItem)?.isChecked = true
            }
        }

        onChecked(selectedItem)
    }

    fun getCategoryId(instance: ListItemUnify) = with(instance) {
        listActionText?.toLong().orZero()
    }
}
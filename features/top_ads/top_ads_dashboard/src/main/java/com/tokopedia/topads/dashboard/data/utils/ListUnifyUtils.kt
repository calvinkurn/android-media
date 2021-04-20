package com.tokopedia.topads.dashboard.data.utils
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify

/**
 * Created by Pika on 7/10/20.
 */

object ListUnifyUtils {

    fun ListUnify.setSelectedItem(items: List<ListItemUnify>, position: Int, onChecked: (selectedItem: ListItemUnify) -> Unit?) = run {
        val selectedItem = this.getItemAtPosition(position) as ListItemUnify
        items.filter { it.getShownRadioButton()?.isChecked ?: false }
                .filterNot { it == selectedItem }
                .onEach { it.getShownRadioButton()?.isChecked = false }
        selectedItem.getShownRadioButton()?.isChecked = true
        onChecked(selectedItem)
    }

    fun ListItemUnify.getShownRadioButton() = run {
        when {
            listLeftRadiobtn?.visibility == android.view.View.VISIBLE -> listLeftRadiobtn
            listRightRadiobtn?.visibility == android.view.View.VISIBLE -> listRightRadiobtn
            else -> null
        }
    }
}
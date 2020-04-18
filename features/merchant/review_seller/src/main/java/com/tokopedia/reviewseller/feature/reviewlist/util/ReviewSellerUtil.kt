package com.tokopedia.reviewseller.feature.reviewlist.util

import android.widget.ListView
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.util.*

object ReviewSellerUtil {

    fun setSelectedFilterOrSort(listUnify: ListUnify, position: Int) {
        with(listUnify) {
            val clickedItem = this.getItemAtPosition(position) as ListItemUnify
            when (choiceMode) {
                ListView.CHOICE_MODE_SINGLE -> {
                    (listUnify.getValue("array") as ArrayList<ListItemUnify>).filter { it.listRightRadiobtn?.isChecked ?: false }
                            .filterNot { it == clickedItem }
                            .onEach { it.listRightRadiobtn?.isChecked = false }

                    clickedItem.listRightRadiobtn?.isChecked = true
                }
            }
        }
    }
}

fun Map<String, String>.getKeyByValue(value: String?): String {
    return this.filterValues { it == value }.keys.firstOrNull().orEmpty()
}

fun <T : Any> T.getValue(field: String): Any {
    return this.javaClass.getDeclaredField(field).apply { isAccessible = true }.get(this)
}
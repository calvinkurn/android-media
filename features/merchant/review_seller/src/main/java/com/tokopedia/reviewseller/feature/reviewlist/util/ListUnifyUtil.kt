package com.tokopedia.reviewseller.feature.reviewlist.util

import android.widget.ListView
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.util.*

object ListUnifyUtil {

    fun setSelectedFilterOrSort(listUnify: ListUnify, position: Int) {
        with(listUnify) {
            val clickedItem = this.getItemAtPosition(position) as ListItemUnify
            when (choiceMode) {
                ListView.CHOICE_MODE_SINGLE -> {
                    getItems(this).filter { it.listRightRadiobtn?.isChecked ?: false }
                            .filterNot { it == clickedItem }
                            .onEach { it.listRightRadiobtn?.isChecked = false }

                    clickedItem.listRightRadiobtn?.isChecked = true
                }
            }
        }
    }

    private fun getItems(instance: ListUnify) = with(instance) {
        this.javaClass.getDeclaredField("array").let {
            it.isAccessible = true
            it.get(this) as ArrayList<ListItemUnify>
        }
    }
}
package com.tokopedia.reviewseller.common.util

import android.widget.ListView
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.math.RoundingMode
import java.text.DecimalFormat

object ReviewSellerUtil {

    fun setFilterMultipleFormat(old: String, newValue: String): String {
        return String.format("$old,$newValue")
    }

    fun setFilterJoinValueFormat(old: String, newValue: String): String {
        return String.format("$old;$newValue")
    }
}

fun SortFilterItem.toggle() {
    type = if (type == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
    }
}

fun ListUnify.setSelectedFilterOrSort(items: List<ListItemUnify>, position: Int) {
    val clickedItem = this.getItemAtPosition(position) as ListItemUnify
    when (choiceMode) {
        ListView.CHOICE_MODE_SINGLE -> {
            items.filter {
                it.listRightRadiobtn?.isChecked ?: false
            }.filterNot { it == clickedItem }.onEach { it.listRightRadiobtn?.isChecked = false }

            clickedItem.listRightRadiobtn?.isChecked = true
        }
    }
}

fun Float?.roundDecimal(): String {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).isDecimalLengthOne()
}

fun String?.isDecimalLengthOne(): String {
    return if(this?.length == 1) {
        "$this.0"
    } else {
        "$this"
    }
}

fun Map<String, Any>.getKeyByValue(value: String?): String {
    return this.filterValues { it == value }.keys.firstOrNull().orEmpty()
}

fun Map<String, Any>.getValueByKey(key: String?): Any? {
    return this.filterKeys { it == key }.values.firstOrNull()
}

fun Map<String, List<Any>>.getValueListByKey(key: String?): List<Any>? {
    return this.filterKeys { it == key }.values.firstOrNull()
}

fun Map<String, Map<String, Any>>.geValueMapByKey(key: String?): Map<String, Any>? {
    return this.filterKeys { it == key }.values.firstOrNull()
}
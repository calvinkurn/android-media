package com.tokopedia.reviewseller.common.util

import android.os.Build
import android.text.Spanned
import android.widget.ListView
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.relativeDate
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object ReviewSellerUtil {

    fun setFilterJoinValueFormat(old: String, newValue: String = ""): String {
        return if (newValue.isNotEmpty()) {
            String.format("$old;$newValue")
        } else
            String.format(old)
    }

    fun getDateChipFilterPosition(data: Array<String>, dateKeyword: String): Int {
        return data.indexOf(dateKeyword)
    }
}

fun getReviewStar(ratingCount: Int): Int {
    return when (ratingCount) {
        1 -> {
            R.drawable.ic_rating_star_one
        }
        2 -> {
            R.drawable.ic_rating_star_two
        }
        3 -> {
            R.drawable.ic_rating_star_three
        }
        4 -> {
            R.drawable.ic_rating_star_four
        }
        5 -> {
            R.drawable.ic_rating_star_five
        }
        else -> {
            R.drawable.ic_rating_star_zero
        }
    }
}

fun String.toReviewDescriptionFormatted(maxChar: Int): Spanned {
    return if (MethodChecker.fromHtml(this).toString().length > maxChar) {
        val subDescription = MethodChecker.fromHtml(this).toString().substring(0, maxChar)
        MethodChecker
                .fromHtml(subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                        + "<font color='#42b549'>Selengkapnya</font>")
    } else {
        MethodChecker.fromHtml(this)
    }
}

infix fun String.toRelativeDate(format: String): String {
    return if (this.isNotEmpty()) {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date: Date = sdf.parse(this)
        val millis: Long = date.time

        return try {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            cal.time.relativeDate
        } catch (t: Throwable) {
            ""
        }
    } else {
        ""
    }
}

fun SortFilterItem.toggle() {
    type = if (type == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
    }
}

fun ChipsUnify.toggle() {
    chipType = if (chipType == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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

fun ListUnify.setSelectedFilterCheckbox(items: List<ListItemRatingWrapper>, position: Int): List<ListItemRatingWrapper> {
    val clickedItem = this.getItemAtPosition(position) as? ListItemUnify
    val isSelected = clickedItem?.listLeftCheckbox?.isSelected ?: false
    return items.mapIndexed { index, item ->
        when (choiceMode) {
            ListView.CHOICE_MODE_MULTIPLE -> {
                item.listItemUnify?.listLeftCheckbox?.isChecked = item.listItemUnify?.listLeftCheckbox?.isChecked != isSelected
                item.sortValue = if (item.listItemUnify?.listLeftCheckbox?.isChecked != isSelected) (index + 1).toString() else ""
                item.isSelected = item.listItemUnify?.listLeftCheckbox?.isChecked != isSelected
            }
        }
        item
    }
}

fun ListUnify.resetAllFilterCheckbox(items: List<ListItemRatingWrapper>): List<ListItemRatingWrapper> {
    return items.map {
        when (choiceMode) {
            ListView.CHOICE_MODE_MULTIPLE -> {
                it.listItemUnify?.listLeftCheckbox?.isChecked = false
                it.isSelected = false
                it.sortValue = ""
            }
        }
        it
    }
}

fun Float?.roundDecimal(): String {
    val rounded = this?.times(10)?.let { round(it) }?.div(10).toString()
    return rounded.isDecimalLengthOne()
}

fun String?.isDecimalLengthOne(): String {
    return if (this?.length == 1) {
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
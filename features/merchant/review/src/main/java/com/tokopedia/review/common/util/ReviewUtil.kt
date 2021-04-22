package com.tokopedia.review.common.util

import android.content.Context
import android.text.Spanned
import android.util.TypedValue
import android.widget.ListView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.relativeDate
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.R
import com.tokopedia.review.common.util.ReviewConstants.ANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.UNANSWERED_VALUE
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object ReviewUtil {

    fun setFilterJoinValueFormat(old: String, newValue: String = ""): String {
        return if (newValue.isNotEmpty()) {
            String.format("$old;$newValue")
        } else
            String.format(old)
    }

    fun getDateChipFilterPosition(data: Array<String>, dateKeyword: String): Int {
        return data.indexOf(dateKeyword)
    }

    fun convertMapObjectToString(map: HashMap<String, Any>): HashMap<String, String>? {
        val newMap = HashMap<String, String>()
        for ((key, value) in map) {
            newMap[key] = value.toString()
        }
        return newMap
    }

    fun DptoPx(context: Context, dp: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
    }

    fun routeToWebview(context: Context, bottomSheet: BottomSheetUnify?, url: String): Boolean {
        val webviewUrl = String.format("%s?url=%s", ApplinkConst.WEBVIEW, url)
        bottomSheet?.dismiss()
        return RouteManager.route(context, webviewUrl)
    }
}

fun getReviewStar(ratingCount: Int): Int {
    return when (ratingCount) {
        1 -> {
            R.drawable.review_ic_rating_star_one
        }
        2 -> {
            R.drawable.review_ic_rating_star_two
        }
        3 -> {
            R.drawable.review_ic_rating_star_three
        }
        4 -> {
            R.drawable.review_ic_rating_star_four
        }
        5 -> {
            R.drawable.review_ic_rating_star_five
        }
        else -> {
            R.drawable.review_ic_rating_star_zero
        }
    }
}

fun String.toReviewDescriptionFormatted(maxChar: Int, context: Context): CharSequence? {
    return if (MethodChecker.fromHtml(this).toString().length > maxChar) {
        val subDescription = MethodChecker.fromHtml(this).toString().substring(0, maxChar)
        HtmlLinkHelper(context, subDescription.replace("(\r\n|\n)".toRegex(), "<br />") + "... "
                + context.getString(R.string.review_expand)).spannedString
    } else {
        MethodChecker.fromHtml(this)
    }
}

infix fun String.toRelativeDate(format: String): String {
    return if (this.isNotEmpty()) {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = sdf.parse(this)
        val millis: Long = date?.time.orZero()

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

fun String.getStatusFilter(prefix: String): String {
    return this.substringAfterLast(prefix)
}

val String.isAnswered: Boolean
    get() {
        return this == ANSWERED_VALUE
    }

val String.isUnAnswered: Boolean
    get() {
        return this == UNANSWERED_VALUE
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

val MutableList<String>.getGeneratedFilterByText: String
    get() {
        return if (size == 1) {
            firstOrNull().toString()
        } else {
            joinToString(separator = ";")
        }
    }

fun MutableList<String>.removeFilterElement(regex: String) {
    this.removeAll {
        it.contains(regex)
    }
}

fun MutableList<String>.getGeneratedTimeFilterByText(prefixTime: String): String {
    return this.find { it.contains(prefixTime) } ?: ""
}

val List<SortItemUiModel>.getSortBy: String
    get() {
        return this.firstOrNull { it.isSelected }?.title.orEmpty()
    }
package com.tokopedia.tokopoints.view.util

import android.view.View
import com.tokopedia.unifyprinciples.Typography

private val LIST_TAG_START = "<li>"
private val LIST_TAG_END = "</li>"
private val MAX_POINTS_TO_SHOW = 4

public fun getLessDisplayData(data: String, seeMore: Typography): String {
    val totalString = data.split(LIST_TAG_START.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    var displayString = totalString[0] + LIST_TAG_START
    if (totalString.size > MAX_POINTS_TO_SHOW + 1) {
        for (i in 1 until MAX_POINTS_TO_SHOW) {
            displayString = displayString + (totalString[i] + LIST_TAG_START)
        }
        displayString = displayString + totalString[MAX_POINTS_TO_SHOW]
        val lastString = totalString[totalString.size - 1]
        if (lastString.contains(LIST_TAG_END)) {
            displayString = displayString + LIST_TAG_END + totalString[totalString.size - 1].split(LIST_TAG_END.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
        }
    } else {
        displayString = data
        seeMore.visibility = View.GONE
    }
    return displayString
}
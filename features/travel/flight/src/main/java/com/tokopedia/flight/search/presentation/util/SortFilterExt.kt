package com.tokopedia.flight.search.presentation.util

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * @author by furqan on 05/03/2020
 */

fun SortFilterItem.select() {
    type = ChipsUnify.TYPE_SELECTED
}

fun SortFilterItem.unselect() {
    type = ChipsUnify.TYPE_NORMAL
}

fun ChipsUnify.select() {
    chipType = ChipsUnify.TYPE_SELECTED
}

fun ChipsUnify.unselect() {
    chipType = ChipsUnify.TYPE_NORMAL
}
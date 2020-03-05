package com.tokopedia.flight.search.util

import com.tokopedia.sortfilter.SortFilterItem

/**
 * @author by furqan on 05/03/2020
 */

fun SortFilterItem.select() {
    type = com.tokopedia.unifycomponents.ChipsUnify.TYPE_SELECTED
}

fun SortFilterItem.unselect() {
    type = com.tokopedia.unifycomponents.ChipsUnify.TYPE_NORMAL
}
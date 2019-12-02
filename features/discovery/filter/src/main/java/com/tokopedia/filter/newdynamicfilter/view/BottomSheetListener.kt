package com.tokopedia.filter.newdynamicfilter.view

import com.tokopedia.filter.common.data.Filter

import java.util.ArrayList

interface BottomSheetListener {
    fun loadFilterItems(filters: ArrayList<Filter>, searchParameter: Map<String, String>)

    fun setFilterResultCount(formattedResultCount: String)

    fun launchFilterBottomSheet()
}

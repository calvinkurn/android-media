package com.tokopedia.filter.newdynamicfilter.view

import com.tokopedia.filter.common.data.Filter

import java.util.ArrayList

/**
 * Created by henrypriyono on 12/03/18.
 */

interface BottomSheetListener {
    fun loadFilterItems(filters: ArrayList<Filter>, searchParameter: Map<String, String>)

    fun setFilterResultCount(formattedResultCount: String)

    fun launchFilterBottomSheet()
}
